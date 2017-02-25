package com.example.crunky.smartminifab;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectStreamException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.*;

/**
 * Created by i00201574 on 20.12.2016.
 * Component is responsible for stock handling - block exchange.
 * The seedbox just gets a handle to IDispatchBlocks. Blocks can be
 * allocated from te Blockfactory (if on stock) or released (back on stock)
 * Component is handld as a singleton - there is just one stock.
 * ResetFactory removes all components on stock
 * AddBlock(s) can be used to create blocks and "fill" the stock.
 */

interface IDispatchBlocks {
    public Block Allocate(BlockShape shape, BlockColor color);
    public void ReleaseBlock(Block block);
    public boolean IsBlocktypeAvailable(BlockShape blockshape, BlockColor color);
}

public class CBlockFactory implements IDispatchBlocks, java.io.Serializable {
    public enum FactoryState{
        UNINITIALIZED,
        OK,
        OUTOFMEMORY,
        NOTABLOCKOBJECT,
        BLOCKNOTAVAILABLE,
        MAXNOBLOCKSEXCEEDED,
        INTERNALERROR,
        SERIALIZATIONERROR,
        FILENOTFOUND,
        INVALIDFILEFORMAT,
        INVALIDFILEVERSION,
    };  // indicates state of the fab
    public final int m_MaxNoOfEachBlockType=99;
    private final int m_FactoryVersion=10000;
    private final int m_FactoryID=0x5ac7081;
    private final String m_FactoryDescription = new String("BlockFactory 00.00.1 Build 1");
    private final String DEF_IO_MSG = "No error.";
    private final String UNKNOWNFILEFORMAT = "File format is unknown.";
    private final String UNKNOWNFILEDESCRIPION = "Factory description is unknown.";
    private final String UNKNOWNFACTORYVERSION = "Factory version is unknown.";
    private String m_IOError;

    private final int MAXNOBLOCKS = Integer.MAX_VALUE; //maximum number of blocks/storae space

    private static CBlockFactory fab = new CBlockFactory(); // Meyers singleton
                                                            // there is only one class to administer
                                                            // the storage

    private FactoryState eFactoryState = FactoryState.UNINITIALIZED;
                                                            // internal state of factory
    private int m_NoBlocks;                                 // overall number of blocks

    private int[][] m_BlocksOnStock = new
            int [BlockType.NODIFFERENTBLOCKTYPES][BlockType.NOBLOCKCOLORS];
                                                            // number of blocks still available
                                                            // for seedbox
    private int[][] m_PlacedBlocks = new
            int [BlockType.NODIFFERENTBLOCKTYPES][BlockType.NOBLOCKCOLORS];
                                                            // number of bocks allocated by the
                                                            // seedbox. Ownership has the seedbox
    transient private List<Block>[][] m_AvailableBlocks;    // List of blocks on stock
    transient private List<Block>[][] m_DrawnBlocks;        // List of placed blocks (just for
                                                            // balancing)
    private IFabCommunication m_fabCommunication;

    private CBlockFactory() {
        m_NoBlocks = 0;                                     // initial number of blocks is zero
        m_IOError = DEF_IO_MSG;
                                                            // populate lists
        m_AvailableBlocks = new ArrayList[BlockType.NODIFFERENTBLOCKTYPES]
                [BlockType.NOBLOCKCOLORS];
        m_DrawnBlocks = new ArrayList[BlockType.NODIFFERENTBLOCKTYPES]
                [BlockType.NOBLOCKCOLORS];

        for (int i = 0; i < Block.NODIFFERENTBLOCKTYPES; ++i) {
            for (int j = 0; j < Block.NOBLOCKCOLORS; ++j) {
                m_AvailableBlocks[i][j] = new ArrayList<Block>();
                m_DrawnBlocks[i][j] = new ArrayList<Block>();
            }
        }
    }

    /**
     * Serialization with Singletons
     * @return
     */
    private Object readResolve() throws ObjectStreamException {
        return fab;
    }

    public static CBlockFactory getInstance() {
        return fab;
    }

    public FactoryState GetFactoryState() {
         return eFactoryState;
    }
    public String GetIOStatusMessage() {return m_IOError;}

    public int GetNoBlocks() {
        return m_NoBlocks;
    }         //number of blocks o stock

    public int GetNoBlocksAvailable(BlockShape blockshape, BlockColor color) {
        int retVal = 0;
        if (m_BlocksOnStock!=null) {
            if (blockshape.ordinal() < m_BlocksOnStock.length &&
                    color.ordinal() < m_BlocksOnStock[0].length) {
                retVal = m_BlocksOnStock[blockshape.ordinal()][color.ordinal()];
            }
        }
        return retVal;
    }

    public int GetNoBlocksDrawn(BlockShape blockshape, BlockColor color) { //number of blocks o stock
        return m_PlacedBlocks[blockshape.ordinal()][color.ordinal()];
    }

    /**
     * returns the number of blocks in an array.
     * Ugly... (are we really writing OO)
     * @return
     */
    public int[][] GetStock() {
        return m_BlocksOnStock;
    }

    /**
     * Used by the grid. Allocates a block (if available)
     * @param blockshape
     * @param color
     * @return Block
     */
    public Block Allocate(BlockShape blockshape, BlockColor color) {
        Block block = null;
        if (m_BlocksOnStock[blockshape.ordinal()][color.ordinal()] > 0) {
            List<Block> blocklist =
                    m_AvailableBlocks[blockshape.ordinal()][color.ordinal()];

            if (blocklist != null && !blocklist.isEmpty()) {
                block = blocklist.remove(0);
                if (block != null) {
                    m_BlocksOnStock[blockshape.ordinal()][color.ordinal()]
                        = blocklist.size();

                    blocklist
                        = m_DrawnBlocks[blockshape.ordinal()][color.ordinal()];

                    if ( blocklist != null ) {
                        blocklist.add(block);
                        m_PlacedBlocks[blockshape.ordinal()][color.ordinal()] = blocklist.size();
                    }
                }
            }
        }
        return block;
    }

    /**
     * Required by seed box. If a block is deleted from the seed box you should call
     * ReleaseBlock to update the stock (new block available). Balancing
     * @param block
     */
    public void ReleaseBlock(Block block) {
        List<Block> blocklist =
                m_DrawnBlocks[block.getShape().ordinal()][block.getColor().ordinal()];

        if (blocklist != null && !blocklist.isEmpty()) {
            for (int i = 0; i != blocklist.size(); ++i) {
                if (blocklist.get(i) == block) {
                    blocklist.remove(i);
                    m_PlacedBlocks[block.getShape().ordinal()][block.getColor().ordinal()] =
                            blocklist.size();
                    blocklist =
                            m_AvailableBlocks[block.getShape().ordinal()][block.getColor().ordinal()];
                    if (blocklist != null) {
                        blocklist.add(block);
                        m_BlocksOnStock[block.getShape().ordinal()][block.getColor().ordinal()] =
                                blocklist.size();
                    }
                    break;
                }
            }
        }
    }

    /**
     * Determines if a bock is available for placing
     */
    public boolean IsBlocktypeAvailable(BlockShape blockshape, BlockColor color) {
        return m_BlocksOnStock[blockshape.ordinal()][color.ordinal()] > 0;
    }

    /**
     * deletes all elements of the factory. Storage size is zero
     */
    public void ResetFactory() {
        m_NoBlocks = 0;
        m_IOError = DEF_IO_MSG;
        for (int i = 0;i < m_BlocksOnStock.length; ++i) {
            for (int j=0;j<m_BlocksOnStock[i].length; ++j)
                m_BlocksOnStock[i][j] = 0;
        }

        for (int i = 0;i < m_PlacedBlocks.length; ++i) {
            for (int j = 0; j < m_PlacedBlocks[i].length; ++j)
                m_PlacedBlocks[i][j] = 0;
        }

        for (int i = 0; i < m_AvailableBlocks.length; ++i) {
            for (int j=0; j < m_AvailableBlocks[i].length; ++j)
                m_AvailableBlocks[i][j].clear();
        }

        for (int i = 0; i<m_DrawnBlocks.length; ++i) {
            for (int j = 0; j < m_DrawnBlocks[i].length; ++j)
                m_DrawnBlocks[i][j].clear();
        }
    }

    /**
     * Used to create blocks. The number of blocks are managed by the factory.
     * Allocating blocks decreases the number of available blocks
     * Release blocks increases the number of available blocks
     * Available blocks can be placed in the seedbox. This function just instantiates
     * the number of specified blocks
     * @param NoBlocks
     * @param blocktype
     * @param color
     * @return
     */
    public int AddBlocks(int NoBlocks, BlockShape blocktype, BlockColor color) {
        //lazy version
        int NoBlocksRegistered=0;
        if (NoBlocks>0 && NoBlocks < MAXNOBLOCKS) {
            int i=0;
            for (i=0; i < NoBlocks; ++i) {
                AddBlock(blocktype, color);
            }
            NoBlocksRegistered = i;
        }
        return NoBlocksRegistered;
    }

    /**
     * Instantiate one block.
     * @param blocktype
     * @param color
     */
    public void AddBlock(BlockShape blocktype, BlockColor color) {
            try {
                Block block = new Block(blocktype, color, BlockRotation.DEGREES_0);
                List<Block> blocklist = m_AvailableBlocks[blocktype.ordinal()][color.ordinal()];

                if (block != null && blocklist != null) {
                    blocklist.add(block);
                    ++m_BlocksOnStock[blocktype.ordinal()][color.ordinal()];
                    ++m_NoBlocks;
                    eFactoryState = FactoryState.OK;
                } else {
                    eFactoryState = FactoryState.INTERNALERROR;
                }
            }
            catch (OutOfMemoryError E) {
                eFactoryState = FactoryState.OUTOFMEMORY;
            }

            catch (IndexOutOfBoundsException e) {
                eFactoryState = FactoryState.INTERNALERROR;
            }
        }

    /**
     * Used to create blocks. The number of blocks are managed by the factory.
     * Allocating blocks decreases the number of available blocks
     * Release blocks increases the number of available blocks
     * Available blocks can be placed in the seedbox. This function just instantiates
     * the number of specified blocks
     * @param NoBlocks
     * @param blocktype
     * @param color
     * @return
     */
    private int RecreateBlocks(int NoBlocks, BlockShape blocktype, BlockColor color) {
        //lazy version
        int NoBlocksRegistered=0;
        if (NoBlocks>0 && NoBlocks < MAXNOBLOCKS) {
            int i=0;
            for (i=0; i < NoBlocks; ++i) {
                RecreateBlock(blocktype, color);
            }
            NoBlocksRegistered = i;
        }
        return NoBlocksRegistered;
    }

    /**
     * Recreate one block.
     * @param blocktype
     * @param color
     */
    private void RecreateBlock(BlockShape blocktype, BlockColor color) {
        try {
            Block block = new Block(blocktype, color, BlockRotation.DEGREES_0);
            List<Block> blocklist = m_AvailableBlocks[blocktype.ordinal()][color.ordinal()];

            if (block != null && blocklist != null) {
                blocklist.add(block);
                eFactoryState = FactoryState.OK;
            } else {
                eFactoryState = FactoryState.INTERNALERROR;
            }
        }
        catch (OutOfMemoryError E) {
            eFactoryState = FactoryState.OUTOFMEMORY;
        }

        catch (IndexOutOfBoundsException e) {
            eFactoryState = FactoryState.INTERNALERROR;
        }
    }

    /**
     * DeleteBlock if available
     * @param blocktype
     * @param color
     */
    public boolean DeleteBlock(BlockShape blocktype, BlockColor color) {
        boolean retVal=false;
        List<Block> blocklist = m_AvailableBlocks[blocktype.ordinal()][color.ordinal()];
        if (blocklist != null && !blocklist.isEmpty()) {
            blocklist.remove(0);
            --m_BlocksOnStock[blocktype.ordinal()][color.ordinal()];
            --m_NoBlocks;
            eFactoryState = FactoryState.OK;
        } else {
            eFactoryState = FactoryState.BLOCKNOTAVAILABLE;
        }
        return retVal;
    }

    /**
     * RecreateStock - required for serialization. It turn out that the standard approach has
     * several drawbacks that are not acceptable
     * @param blocktype
     * @param color
     */
    private void RecreateStock() {
        m_NoBlocks = 0;

        m_AvailableBlocks = new ArrayList[BlockType.NODIFFERENTBLOCKTYPES]
                [BlockType.NOBLOCKCOLORS];
        m_DrawnBlocks = new ArrayList[BlockType.NODIFFERENTBLOCKTYPES]
                [BlockType.NOBLOCKCOLORS];

        for (int i = 0; i < Block.NODIFFERENTBLOCKTYPES; ++i) {
            for (int j = 0; j < Block.NOBLOCKCOLORS; ++j) {
                m_AvailableBlocks[i][j] = new ArrayList<Block>();
                m_DrawnBlocks[i][j] = new ArrayList<Block>();
            }
        }

        for (int i = 0;i < m_BlocksOnStock.length; ++i) {
            for (int j=0;j<m_BlocksOnStock[i].length; ++j) {
                m_BlocksOnStock[i][j] += m_PlacedBlocks[i][j];
                m_NoBlocks += m_BlocksOnStock[i][j];
                m_PlacedBlocks[i][j] = 0;
            }
        }

        for (int i = 0; i < m_AvailableBlocks.length; ++i) {
            for (int j=0; j < m_AvailableBlocks[i].length; ++j)
                m_AvailableBlocks[i][j].clear();
        }

        for (int i = 0; i<m_DrawnBlocks.length; ++i) {
            for (int j = 0; j < m_DrawnBlocks[i].length; ++j)
                m_DrawnBlocks[i][j].clear();
        }

        for (BlockShape shape : BlockShape.values()) {
            for (BlockColor color : BlockColor.values())
                if (m_BlocksOnStock[shape.ordinal()][color.ordinal()] > 0) {
                    RecreateBlocks(m_BlocksOnStock[shape.ordinal()][color.ordinal()],
                            shape, color);
                }
        }
    }

    /**
     * handling for serialization
     * @param out
     * @throws IOException
     */
    public boolean writeObject(String filename)  {
        boolean retVal = false;
        m_IOError = DEF_IO_MSG;
        try {
            File File = new File(filename);
            File.createNewFile();

            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeInt(m_FactoryID);
            out.writeObject(m_FactoryDescription);
            out.writeObject(m_FactoryVersion);
            out.writeObject(m_NoBlocks);
            out.writeObject(m_BlocksOnStock);
            //out.writeObject(m_PlacedBlocks);
            out.close();
            fileOut.close();
            retVal = true;
            // message supi
        }catch(IOException i) {
            // message oh no...
            // i.printStackTrace();
            eFactoryState = FactoryState.SERIALIZATIONERROR;
            m_IOError = i.getMessage();
        }
        return retVal;
    }

    /**
     * Serialization
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean readObject(String filename) {
        boolean retVal = false;
        m_IOError = DEF_IO_MSG;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            int factoryid = in.readInt();
            if (factoryid==m_FactoryID) {
                String FactoryDescription = (String) in.readObject();
                if (m_FactoryDescription.equals(FactoryDescription)) {
                    int FactoryVersion = (int) in.readObject();
                    if (FactoryVersion == m_FactoryVersion) {
                        int NoBlocks = (int) in.readObject();
                        int[][] BlocksOnStock = (int[][]) in.readObject();
                        //m_PlacedBlocks = (int[][]) in.readObject();
                        int[][] PlacedBlocks = new int[m_BlocksOnStock.length][m_BlocksOnStock[0].length];

                        eFactoryState = FactoryState.UNINITIALIZED;
                        m_NoBlocks = NoBlocks;
                        m_BlocksOnStock = BlocksOnStock;
                        m_PlacedBlocks = PlacedBlocks;
                        RecreateStock();
                        eFactoryState = FactoryState.OK;
                        retVal = true;
                    } else {
                        m_IOError = UNKNOWNFACTORYVERSION;
                        eFactoryState = FactoryState.INVALIDFILEVERSION;
                    }
                } else {
                    m_IOError = UNKNOWNFILEDESCRIPION;
                    eFactoryState = FactoryState.INVALIDFILEFORMAT;
                }
            } else {
                m_IOError = UNKNOWNFILEFORMAT;
                eFactoryState = FactoryState.INVALIDFILEFORMAT;
            }
            in.close();
            fileIn.close();
        }

        catch (ClassNotFoundException cnf) {
            eFactoryState = FactoryState.SERIALIZATIONERROR;
            m_IOError = cnf.getMessage();
        }
        catch (IOException i) {
            //ResetFactory();
            eFactoryState = FactoryState.FILENOTFOUND;
            m_IOError = i.getMessage();
        }
        return retVal;
    }

    public void setFabCommunication(IFabCommunication fabCommunication) {
        m_fabCommunication=fabCommunication;
    }

    public IFabCommunication getFabCommunication() {
        return m_fabCommunication;
    }
}

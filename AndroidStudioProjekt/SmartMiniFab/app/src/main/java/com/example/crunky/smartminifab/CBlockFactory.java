package com.example.crunky.smartminifab;
import java.util.*;

/**
 * Created by i00201574 on 20.12.2016.
 */

interface IDispatchBlocks {
    public Block Allocate(BlockShape shape, BlockColor color);
    public void ReleaseBlock(Block block);
}

public class CBlockFactory implements IDispatchBlocks {
    public enum FactoryState{
        UNINITIALIZED,
        OK,
        OUTOFMEMORY,
        NOTABLOCKOBJECT,
        MAXNOBLOCKSEXCEEDED,
        INTERNALERROR
    };  // indicates state of the fab

    private final int MAXNOBLOCKS = 10; //maximum number of blocks/storae space


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
    private List<Block>[][] m_AvailableBlocks;              // List of blocks on stock
    private List<Block>[][] m_DrawnBlocks;                  // List of placed blocks (just for
                                                            // balancing)

    private CBlockFactory() {
        m_NoBlocks = 0;                                     // initial number of blocks is zero
                                                            // populate lists
        m_AvailableBlocks = new ArrayList[BlockType.NODIFFERENTBLOCKTYPES]
                [BlockType.NODIFFERENTBLOCKTYPES];
        m_DrawnBlocks = new ArrayList[BlockType.NODIFFERENTBLOCKTYPES]
                [BlockType.NODIFFERENTBLOCKTYPES];

        for (int i = 0; i < Block.NODIFFERENTBLOCKTYPES; ++i) {
            for (int j = 0; j < Block.NOBLOCKCOLORS; ++j)
                m_AvailableBlocks[i][j] = new ArrayList<Block>();
        }
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
     * deletes all elements of the factory. Storage size is zero
     */
    public void ResetFactory() {
        m_NoBlocks = 0;

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
}

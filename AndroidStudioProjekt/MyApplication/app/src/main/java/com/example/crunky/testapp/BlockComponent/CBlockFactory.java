package BlockComponent;
import java.util.*;

/**
 * Created by i00201574 on 20.12.2016.
 */
interface IDispatchBlocks {
    public CBlock Allocate(CBlock.eBlockType blocktype, CBlock.eBlockColor color);
    public void ReleaseBlock(CBlock block);
}

public class CBlockFactory implements IDispatchBlocks {
    public enum FactoryState{UNINITIALIZED, OK, OUTOFMEMORY, NOTABLOCKOBJECT, MAXNOBLOCKSEXCEEDED,
        INTERNALERROR};
    private final int MAXNOBLOCKS=10;
    private static CBlockFactory fab = new CBlockFactory();

    private CBlockFactory() {
            m_ObjectIdHandler=0;
            m_NoBlocks=0;
            m_AvailableBlocks = new ArrayList<List<CBlock>>();
            m_DrawnBlocks = new ArrayList<List<CBlock>>();
            for (int i=0; i < CBlock.NODIFFERENTBLOCKTYPES; ++i)
            {
                m_AvailableBlocks.add(new ArrayList<CBlock>());
                m_DrawnBlocks.add(new ArrayList<CBlock>());
            }
        }
    public static CBlockFactory getInstance() {
        return fab;
    }

    private FactoryState eFactoryState=FactoryState.UNINITIALIZED;
    private int m_ObjectIdHandler;

    private int   m_NoBlocks;
    private int[] m_BlocksOnStock = new int [CBlock.NODIFFERENTBLOCKTYPES];
    private int[] m_PlacedBlocks = new int [CBlock.NODIFFERENTBLOCKTYPES];
    private List<List<CBlock>> m_AvailableBlocks;
    private List<List<CBlock>> m_DrawnBlocks;

    public CBlock Allocate(CBlock.eBlockType blocktype, CBlock.eBlockColor color) {
        CBlock block = null;
        if (m_BlocksOnStock[blocktype.ordinal()]>0) {
            for (int i=0; i<m_AvailableBlocks.get(blocktype.ordinal()).size();++i) {
                if (m_AvailableBlocks.get(blocktype.ordinal()).get(i).GetColor()==color) {
                    block = m_AvailableBlocks.get(blocktype.ordinal()).get(i);
                    m_AvailableBlocks.get(blocktype.ordinal()).remove(i);
                    m_DrawnBlocks.get(blocktype.ordinal()).add(block);
                }
            }
        }
        return block;
    }

    public void ReleaseBlock(CBlock block) {
        if (block!=null) {
            block.Rotate(CBlock.eRotation.DEGREES_0);
            m_AvailableBlocks.get(block.GetBlockType().ordinal()).add(block);
            for (int i=0;i<m_DrawnBlocks.get(block.GetBlockType().ordinal()).size();++i)
                if (m_DrawnBlocks.get(block.GetBlockType().ordinal()).get(i).GetUniqueObjectId()==block.GetUniqueObjectId()) {
                    m_DrawnBlocks.get(block.GetBlockType().ordinal()).remove(i);
                }
        }
    }
    public void ResetFactory() {
        m_ObjectIdHandler = 0;
        m_NoBlocks = 0;
        for (int i=0;i<m_BlocksOnStock.length;++i) {
            m_BlocksOnStock[i] = 0;
        }

        for (int i=0;i<m_PlacedBlocks.length;++i) {
            m_PlacedBlocks[i] = 0;
        }

        //m_AvailableBlocks.forEach( temp -> {
        //    temp.clear();
        //})
        for (int i=0; i<m_AvailableBlocks.size();++i) {
            m_AvailableBlocks.get(i).clear();
        }

        for (int i=0; i<m_DrawnBlocks.size();++i) {
            m_DrawnBlocks.get(i).clear();
        }
    }

    public int AddBlocks(int NoBlocks, CBlock.eBlockType blocktype, CBlock.eBlockColor color) {
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
    public void AddBlock(CBlock.eBlockType blocktype, CBlock.eBlockColor color) {
            boolean succ = true;
            CBlock newblock;
            try {
                switch (blocktype) {
                    case SIMPLE_SQUARE:
                        newblock = new CSimpleSquare(color, ++m_ObjectIdHandler);
                        m_AvailableBlocks.get(blocktype.ordinal()).add(newblock);
                        ++m_BlocksOnStock[blocktype.ordinal()];
                        ++m_NoBlocks;
                        break;
                    case QUADRUPLE_SQUARE:
                        newblock = new CQuadrupleSquare(color, ++m_ObjectIdHandler);
                        m_AvailableBlocks.get(blocktype.ordinal()).add(newblock);
                        ++m_BlocksOnStock[blocktype.ordinal()];
                        ++m_NoBlocks;
                        break;
                    case I_SHAPE:
                        newblock = new CIShape(color, ++m_ObjectIdHandler);
                        m_AvailableBlocks.get(blocktype.ordinal()).add(newblock);
                        ++m_BlocksOnStock[blocktype.ordinal()];
                        ++m_NoBlocks;
                        break;
                    case MIRRORED_L_SHAPE:
                        newblock = new CMirroredLShape(color, ++m_ObjectIdHandler);
                        m_AvailableBlocks.get(blocktype.ordinal()).add(newblock);
                        ++m_BlocksOnStock[blocktype.ordinal()];
                        ++m_NoBlocks;
                        break;
                    case L_SHAPE:
                        newblock = new CLShape(color, ++m_ObjectIdHandler);
                        m_AvailableBlocks.get(blocktype.ordinal()).add(newblock);
                        ++m_BlocksOnStock[blocktype.ordinal()];
                        ++m_NoBlocks;
                        break;
                    case FOUR_SHAPE:
                        newblock = new CFourShape(color, ++m_ObjectIdHandler);
                        m_AvailableBlocks.get(blocktype.ordinal()).add(newblock);
                        ++m_BlocksOnStock[blocktype.ordinal()];
                        ++m_NoBlocks;
                        break;
                    case MIRRORED_T_SHAPE:
                        newblock = new CMirroredTShape(color, ++m_ObjectIdHandler);;
                        m_AvailableBlocks.get(blocktype.ordinal()).add(newblock);
                        ++m_BlocksOnStock[blocktype.ordinal()];
                        ++m_NoBlocks;
                        break;
                    case MIRRORRED_FOUR_SHAPE:
                        newblock = new CSimpleSquare(color, ++m_ObjectIdHandler);
                        m_AvailableBlocks.get(blocktype.ordinal()).add(newblock);
                        ++m_BlocksOnStock[blocktype.ordinal()];
                        ++m_NoBlocks;
                        break;
                    default:
                        newblock = null;
                        eFactoryState = FactoryState.NOTABLOCKOBJECT;
                        break;
                }
            }
            catch (OutOfMemoryError E) {
                eFactoryState = FactoryState.OUTOFMEMORY;
                succ = false;
            }

            catch (IndexOutOfBoundsException e) {
                eFactoryState = FactoryState.INTERNALERROR;
            }
        }
}

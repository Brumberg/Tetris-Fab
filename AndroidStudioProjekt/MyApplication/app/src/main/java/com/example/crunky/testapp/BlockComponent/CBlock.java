package BlockComponent;

/**
 * Created by i00201574 on 19.12.2016.
 */

public class CBlock{
    public enum eRotation{DEGREES_0, DEGREES_90, DEGREES_180, DEGREES_270}
    public enum eBlockType{SIMPLE_SQUARE, QUADRUPLE_SQUARE, I_SHAPE, MIRRORED_L_SHAPE, L_SHAPE,
        FOUR_SHAPE, MIRRORED_T_SHAPE, MIRRORRED_FOUR_SHAPE}
    public enum eBlockColor{BLACK, RED, GREEN, BLUE, YELLOW}

    public class CCoordinates {
        public int m_X;
        public int m_Y;
        public CCoordinates(int x, int y){m_X=x;m_Y=y;}
        public CCoordinates(){m_X=0;m_Y=0;}
    }

    public static final int NODIFFERENTBLOCKTYPES = eBlockType.values().length; // last of eBlockType+1
    private
        int m_UniqueObjectId;
        eBlockType m_BlockType;
        eBlockColor m_Color;
        eRotation m_Rotation;
        int m_Graphity_X;
        int m_Graphity_Y;
        int m_Dim_X;
        int m_Dim_Y;

    protected
        int [][] m_RawData;

    protected CBlock() {}

    protected CBlock(eBlockType blocktype, eBlockColor color, int xdim, int ydim,
                     eRotation rotation, int graphityx, int graphityy, int unique_id) {
        m_BlockType = blocktype;
        m_Color = color;
        m_Dim_X = xdim;
        m_Dim_Y = ydim;
        m_Rotation = eRotation.DEGREES_0;
        m_Graphity_X = graphityx;
        m_Graphity_Y = graphityy;
        m_UniqueObjectId = unique_id;
    }

    public eBlockColor GetColor() {return m_Color;}
    public eBlockType GetBlockType() {return m_BlockType;}
    public int GetUniqueObjectId(){return m_UniqueObjectId;}
    public int GetWidth(){return m_Dim_X;}
    public int GetHeights(){return m_Dim_Y;}
    public int GetRawData(int x, int y) {if (x>=0&&y>=0&&x<m_Dim_X&&y<m_Dim_Y) {
        return m_RawData[y][x];} else {return -1;}}

    public void Rotate( eRotation rotation ) {
        m_Rotation = rotation;
    }

    public void RotateClockwise() {
        switch (m_Rotation) {
            case DEGREES_0:
                m_Rotation = eRotation.DEGREES_270;
                break;
            case DEGREES_90:
                m_Rotation = eRotation.DEGREES_0;
                break;
            case DEGREES_180:
                m_Rotation = eRotation.DEGREES_90;
                break;
            case DEGREES_270:
                m_Rotation = eRotation.DEGREES_180;
                break;
            default:
                m_Rotation = eRotation.DEGREES_0;
                break;
        }
    }

    public void RotateCounterClockwise() {
        switch (m_Rotation)
        {
            case DEGREES_0:
                m_Rotation = eRotation.DEGREES_90;
                break;
            case DEGREES_90:
                m_Rotation = eRotation.DEGREES_180;
                break;
            case DEGREES_180:
                m_Rotation = eRotation.DEGREES_270;
                break;
            case DEGREES_270:
                m_Rotation = eRotation.DEGREES_0;
                break;
            default:
                m_Rotation = eRotation.DEGREES_0;
                break;
        }
    }

    public int GetRotation()
    {
        int rotation;
        switch (m_Rotation)
        {
            case DEGREES_0:
                rotation = 0;
                break;
            case DEGREES_90:
                rotation = 1;
                break;
            case DEGREES_180:
                rotation = 2;
                break;
            case DEGREES_270:
                rotation = 3;
                break;
            default:
                rotation = 0;
                break;
        }
        return rotation;
    }

    public CCoordinates GetBaseCoords() {
        CCoordinates dummy = new CCoordinates();
        switch (m_Rotation){
            case DEGREES_0:
                dummy.m_X = m_Graphity_X;
                dummy.m_Y = m_Graphity_Y;
                break;
            case DEGREES_90:
                dummy.m_X = GetWidth()-1-m_Graphity_Y;
                dummy.m_Y = m_Graphity_X;
                break;
            case DEGREES_180:
                dummy.m_X = GetWidth()-1-m_Graphity_X;
                dummy.m_Y = GetHeights()-1-m_Graphity_Y;
                break;
            case DEGREES_270:
                dummy.m_X = m_Graphity_Y;
                dummy.m_Y = GetHeights()-1-m_Graphity_X;
                break;
        }
        return dummy;
    }
}
package BlockComponent;

/**
 * Created by i00201574 on 21.12.2016.
 */

public class CGridCtrl {
    public enum eGridSize{THREEBYTHREE, FOURBYTHREE, FIVEBYFOUR, USERDEFINED};
    private IDispatchBlocks m_DispatchIface;
    private final int DEFGRIDSIZEX=3;
    private final int DEFGRIDSIZEY=3;
    private int m_GridX;
    private int m_GridY;
    private int [][]m_Grid;

    private CGridCtrl() {CreateGrid(eGridSize.THREEBYTHREE);}
    public CGridCtrl(IDispatchBlocks dispatch) {
        m_DispatchIface=dispatch;
        CreateGrid(eGridSize.THREEBYTHREE);
    }
    public int GetWidth(){return m_GridX;}
    public int GetHeight(){return m_GridY;}
    public int GetObjectId(int x,int y) {
        if (x<m_GridX&&y<m_GridY) {
            return m_Grid[y][x];
        } else {
            return 0;
        }
    }

    public void CreateGrid(eGridSize grid) {
        switch (grid) {
            case THREEBYTHREE:
                m_GridX=3;
                m_GridY=3;
                break;
            case FOURBYTHREE:
                m_GridX=4;
                m_GridY=3;
                break;
            case FIVEBYFOUR:
                m_GridX=5;
                m_GridY=4;
                break;
            default:
                m_GridX=DEFGRIDSIZEX;
                m_GridY=DEFGRIDSIZEY;
                break;
        }
        //that is due to performance issues normally m_GridY and m_GridX vice versa
        m_Grid = new int [m_GridY][m_GridX];
    }

    public void CreateGrid(int x, int y) {
        if (x>0) {
            m_GridX = x;
        }
        if (y>0) {
            m_GridY = y;
        }
        //that is due to performance issues normally m_GridY and m_GridX vice versa
        m_Grid = new int [m_GridY][m_GridX];
    }

    public boolean EraseBlock(CBlock block) {
        boolean RetVal = false;
        for (int i=0;i<m_Grid.length;++i)
            for (int j=0;i<m_Grid[i].length;++j)
                if (m_Grid[i][j]==block.GetUniqueObjectId()) {
                    m_Grid[i][j] = 0;
                    RetVal = true;
                }
        if (block!=null) {
            m_DispatchIface.ReleaseBlock(block);
        }
        return RetVal;
    }

    public boolean PlaceBlock(CBlock block, CBlock.eRotation rotation, int xcoord, int ycoord) {
        boolean retVal = true;
        switch (rotation)
        {
            case DEGREES_0:
                if (xcoord>=0&&ycoord>=0&&xcoord+block.GetWidth()-1<m_GridX&&
                        ycoord+block.GetHeights()-1<m_GridY) {
                    for (int y=0;y<block.GetHeights();y++) {
                        for (int x=0;x<block.GetWidth();x++) {
                            retVal &= block.GetRawData(x,y)==0||m_Grid[y+ycoord][x+xcoord]==0;
                        }
                    }
                    if (retVal) {
                        for (int y=0;y<block.GetHeights();y++) {
                            for (int x=0;x<block.GetWidth();x++) {
                                m_Grid[y+ycoord][x+xcoord] |= block.GetRawData(x,y);
                            }
                        }
                    }
                    block.Rotate(rotation);
                } else {
                    retVal = false;
                }
                break;
            case DEGREES_180:
                if (xcoord>=0&&ycoord>=0&&xcoord+block.GetWidth()-1<m_GridX&&
                        ycoord+block.GetHeights()-1<m_GridY) {
                    int yhi = block.GetHeights()-1;
                    int xhi = block.GetWidth()-1;
                    for (int y=0;y<block.GetHeights();y++) {
                        for (int x=0;x<block.GetWidth();x++) {
                            retVal &= block.GetRawData(xhi-x,yhi-y)==0||m_Grid[y+ycoord][x+xcoord]==0;
                        }
                    }
                    if (retVal) {
                        for (int y=0;y<block.GetHeights();y++) {
                            for (int x=0;x<block.GetWidth();x++) {
                                m_Grid[y+ycoord][x+xcoord] |= block.GetRawData(xhi-x,yhi-y);
                            }
                        }
                    }
                    block.Rotate(rotation);
                } else {
                    retVal = false;
                }
                break;
            case DEGREES_90:
                if (xcoord>=0&&ycoord>=0&&xcoord+block.GetHeights()-1<m_GridX&&
                        ycoord+block.GetWidth()-1<m_GridY) {
                    int yhi = block.GetWidth()-1;
                    for (int y=0;y<block.GetWidth();y++) {
                        for (int x=0;x<block.GetHeights();x++) {
                            retVal &= block.GetRawData(yhi-y,x)==0||m_Grid[y+ycoord][x+xcoord]==0;
                        }
                    }
                    if (retVal) {
                        for (int y=0;y<block.GetWidth();y++) {
                            for (int x=0;x<block.GetHeights();x++) {
                                m_Grid[y+ycoord][x+xcoord] |= block.GetRawData(yhi-y,x);
                            }
                        }
                    }
                    block.Rotate(rotation);
                } else {
                    retVal = false;
                }
                break;
            case DEGREES_270:
                if (xcoord>=0&&ycoord>=0&&xcoord+block.GetHeights()-1<m_GridX&&
                        ycoord+block.GetWidth()-1<m_GridY) {
                    int xhi = block.GetHeights()-1;
                    for (int y=0;y<block.GetWidth();y++) {
                        for (int x=0;x<block.GetHeights();x++) {
                            retVal &= block.GetRawData(y,xhi-x)==0||m_Grid[y+ycoord][x+xcoord]==0;
                        }
                    }
                    if (retVal) {
                        for (int y=0;y<block.GetWidth();y++) {
                            for (int x=0;x<block.GetHeights();x++) {
                                m_Grid[y+ycoord][x+xcoord] |= block.GetRawData(y,xhi-x);
                            }
                        }
                    }
                    block.Rotate(rotation);
                } else {
                    retVal = false;
                }
                break;
            default:
                break;
        }
        return retVal;
    }

    public boolean IsBlockPlacable(CBlock block, CBlock.eRotation rotation, int xcoord, int ycoord) {
        boolean retVal = true;
        switch (rotation)
        {
            case DEGREES_0:
                if (xcoord>=0&&ycoord>=0&&xcoord+block.GetWidth()-1<m_GridX&&
                        ycoord+block.GetHeights()-1<m_GridY) {
                    for (int y=0;y<block.GetHeights();y++) {
                        for (int x=0;x<block.GetWidth();x++) {
                            retVal &= block.GetRawData(x,y)==0||m_Grid[y+ycoord][x+xcoord]==0;
                        }
                    }
                } else {
                    retVal = false;
                }
                break;
            case DEGREES_180:
                if (xcoord>=0&&ycoord>=0&&xcoord+block.GetWidth()-1<m_GridX&&
                        ycoord+block.GetHeights()-1<m_GridY) {
                    int yhi = block.GetHeights()-1;
                    int xhi = block.GetWidth()-1;
                    for (int y=0;y<block.GetHeights();y++) {
                        for (int x=0;x<block.GetWidth();x++) {
                            retVal &= block.GetRawData(xhi-x,yhi-y)==0||m_Grid[y+ycoord][x+xcoord]==0;
                        }
                    }
                } else {
                    retVal = false;
                }
                break;
            case DEGREES_90:
                if (xcoord>=0&&ycoord>=0&&xcoord+block.GetHeights()-1<m_GridX&&
                        ycoord+block.GetWidth()-1<m_GridY) {
                    int yhi = block.GetWidth()-1;
                    for (int y=0;y<block.GetWidth();y++) {
                        for (int x=0;x<block.GetHeights();x++) {
                            retVal &= block.GetRawData(yhi-y,x)==0||m_Grid[y+ycoord][x+xcoord]==0;
                        }
                    }
                } else {
                    retVal = false;
                }
                break;
            case DEGREES_270:
                if (xcoord>=0&&ycoord>=0&&xcoord+block.GetHeights()-1<m_GridX&&
                        ycoord+block.GetWidth()-1<m_GridY) {
                    int xhi = block.GetHeights()-1;
                    for (int y=0;y<block.GetWidth();y++) {
                        for (int x=0;x<block.GetHeights();x++) {
                            retVal &= block.GetRawData(y,xhi-x)==0||m_Grid[y+ycoord][x+xcoord]==0;
                        }
                    }
                } else {
                    retVal = false;
                }
                break;
            default:
                break;
        }
        return retVal;
    }
}

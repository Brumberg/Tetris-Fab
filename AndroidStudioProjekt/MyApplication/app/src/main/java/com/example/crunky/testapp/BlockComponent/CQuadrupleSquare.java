package BlockComponent;
import java.util.Arrays;
/**
 * Created by i00201574 on 20.12.2016.
 */

public class CQuadrupleSquare extends CBlock {
    public CBlock Create() {
        return new CQuadrupleSquare();
    }
    private CQuadrupleSquare() {}
    private static int m_StaticRawData[][] = {{1,1},{1,1}};
    protected CQuadrupleSquare(CBlock.eBlockColor color, int unique_id) {
        //add code here
        super(eBlockType.QUADRUPLE_SQUARE, color, 2, 2, CBlock.eRotation.DEGREES_0, 0, 1, unique_id);
        m_RawData = new int[2][2];
        for (int i = 0; i < m_StaticRawData.length; i++) {
            for (int j=0; j < m_StaticRawData[i].length; ++j) {
                m_RawData[i][j] = unique_id*m_StaticRawData[i][j];
            }
        }
    }
}

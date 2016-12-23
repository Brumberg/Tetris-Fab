package BlockComponent;
import java.util.Arrays;
/**
 * Created by i00201574 on 20.12.2016.
 */

public class CTestBlock extends CBlock {
    public CBlock Create() {
        return new CTestBlock();
    }
    private CTestBlock() {}
    private static int m_StaticRawData[][] = {{1}};
    protected CTestBlock(CBlock.eBlockColor color, int unique_id) {
        //add code here
        super(eBlockType.SIMPLE_SQUARE, color, 1, 1, CBlock.eRotation.DEGREES_0, 0, 0, unique_id);
        m_RawData = new int[1][1];
        for (int i = 0; i < m_StaticRawData.length; i++) {
            for (int j=0; j < m_StaticRawData[i].length; ++j) {
                m_RawData[i][j] = unique_id*m_StaticRawData[i][j];
            }
        }
    }
}

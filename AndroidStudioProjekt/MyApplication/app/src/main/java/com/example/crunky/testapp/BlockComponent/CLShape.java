package BlockComponent;
import java.util.Arrays;
/**
 * Created by i00201574 on 20.12.2016.
 */

public class CLShape extends CBlock {
    public CBlock Create() {
        return new CLShape();
    }
    private CLShape() {}
    private static int m_StaticRawData[][] = {{1,1},{1,0},{1,0}};
    protected CLShape(CBlock.eBlockColor color, int unique_id) {
        //add code here
        super(eBlockType.L_SHAPE, color, 2, 3, CBlock.eRotation.DEGREES_0, 1, 1, unique_id);
        m_RawData = new int[3][2];
        for (int i = 0; i < m_StaticRawData.length; i++) {
            for (int j=0; j < m_StaticRawData[i].length; ++j) {
                m_RawData[i][j] = unique_id*m_StaticRawData[i][j];
            }
        }
    }
}

package BlockComponent;
import java.util.Arrays;
/**
 * Created by i00201574 on 20.12.2016.
 */

public class CMirroredTShape extends CBlock {
    public CBlock Create() {
        return new CMirroredTShape();
    }

    private CMirroredTShape() {}
    private static int m_StaticRawData[][] = {{1,1,1},{0,1,0}};
    protected CMirroredTShape(CBlock.eBlockColor color, int unique_id) {
        //add code here
        super(eBlockType.MIRRORED_T_SHAPE, color, 3, 2, CBlock.eRotation.DEGREES_0, 1, 0, unique_id);
        m_RawData = new int[3][1];
        for (int i = 0; i < m_StaticRawData.length; i++) {
            m_RawData[i] = Arrays.copyOf(m_StaticRawData[i], m_StaticRawData[i].length);
        }
    }
}

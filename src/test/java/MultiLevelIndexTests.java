import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiLevelIndexTests {

    MultiLevelIndex multiLevelIndex;

    @BeforeEach
    void initializeMultiLevelIndex(){
        multiLevelIndex = new MultiLevelIndex();
    }

    @Test
    void WhenTwiceNextIndexWithIncreasedIndexLevelDepthsThen(){
        assertEquals("1.1.1", multiLevelIndex.nextIndex(1).nextIndex(2).toString());
    }
    @Test
    void WhenFirstNextIndexWithHigherIndexLevelDepthSecondNextIndexWithLowerIndexLevelDepthThen(){
        assertEquals("1.2", multiLevelIndex.nextIndex(2).nextIndex(1).toString());
    }
    @Test
    void WhenTwiceNextIndexWithSameIndexLevelDepthsThen(){
        assertEquals("2", multiLevelIndex.nextIndex(0).nextIndex(0).toString());
    }
    @Test
    void WhenConstructorWithArrayAsParameterThen(){
        MultiLevelIndex multiLevelIndexWithArray = new MultiLevelIndex(new int[]{1,3,1,2});
        assertEquals("1.3.1.2", multiLevelIndexWithArray.toString());
    }
}

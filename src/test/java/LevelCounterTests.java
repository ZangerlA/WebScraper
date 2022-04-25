import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LevelCounterTests {
    LevelCounter levelCounter;
    Element elementHTwo, elementHThree, elementHFour, elementHFive;
    Elements elements;

    @BeforeEach
    void initializeLevelCounter(){
        elementHTwo = new Element("h2");
        elementHThree = new Element("h3");
        elementHFour = new Element("h4");
        elementHFive = new Element("h5");
        elements = new Elements();
        elements.add(elementHTwo);
        elements.add(elementHThree);
        elements.add(elementHFour);
        levelCounter = new LevelCounter(elements);
    }

    @Test
    void WhenGetLevelHeaderWithHThreeThenReturnThree(){
        assertEquals(2,LevelCounter.getHeaderLevel(elementHTwo));
    }
    @Test
    void WhenGetNextIndexLevelOfWithElementHFourThenReturnThree(){
        assertEquals(2,levelCounter.getIndexLevelOf(elementHFour));
    }
    @Test
    void WhenGetNextIndexLevelOfWithElementHFiveThenReturnMinusOneNotInIndexLevels(){
        assertEquals(-1,levelCounter.getIndexLevelOf(elementHFive));
    }

}

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

public class LevelCounter {
    private ArrayList<Integer> indexLevels;

    public LevelCounter(Elements elements){
        indexLevels = createListOfHeaderLevels(elements);
    }

    private static ArrayList<Integer> createListOfHeaderLevels(Elements elements){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (Element element: elements) {
            if (!arrayList.contains(getHeaderLevel(element))){
                arrayList.add(getHeaderLevel(element));
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }
    public int getIndexLevelOf(Element element){
        for (int i = 0; i < indexLevels.size(); i++) {
            if (indexLevels.get(i) == getHeaderLevel(element)){
                return i;
            }
        }
        return -1;
    }

    public static int getHeaderLevel(Element element){
        return Integer.parseInt(element.tagName().substring(element.tagName().length() - 1));
    }
}

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupElements {
    private JsoupDocument document;
    private Elements selectedElements;

    public JsoupElements(JsoupDocument document, String cssQuery){
        this.document = document;
        this.selectedElements = selectElements(document.getDocument(), cssQuery);
    }

    private Elements selectElements(Document document, String cssQuery){
        return document.select(cssQuery);
    }

    public Elements getElements() {
        return selectedElements;
    }
}

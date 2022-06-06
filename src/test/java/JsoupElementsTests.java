import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class JsoupElementsTests {
    static JsoupElements elements;

    @BeforeAll
    public static void init(){
        try {
            JsoupDocument document = new JsoupDocument("https://quotes.toscrape.com/");
            elements = new JsoupElements(document, "h1");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void WhenQueryForHeadersThenReturnHeaders(){
        assertInstanceOf(Elements.class, elements.getElements());
    }
}

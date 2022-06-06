import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsoupDocumentTests {
    JsoupDocument document;

    @BeforeEach
    public void init(){
        try {
            document = new JsoupDocument("https://quotes.toscrape.com/");
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }

    @Test
    public void WhenParseDocumentWithBrokenUrlThenThrowIOException(){
        assertThrows(IOException.class ,()-> new JsoupDocument("abc"));
    }

    @Test
    public void WhenParseDocumentWithUrlThenDoesntThrowIOException(){
        assertDoesNotThrow(()-> new JsoupDocument("https://quotes.toscrape.com/"));
    }

    @Test
    public void WhenParseDocumentWithUrlThenDocumentIsReturned(){
        assertInstanceOf(Document.class, document.getDocument());
    }
}

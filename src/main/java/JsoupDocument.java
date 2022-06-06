import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URL;

public class JsoupDocument {
    private Document document;

    public JsoupDocument(String url) throws IOException {
        this.document = parseDocument(url);
    }

    private Document parseDocument(String url) throws IOException {
        return Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
    }

    public Document getDocument() {
        return document;
    }
}
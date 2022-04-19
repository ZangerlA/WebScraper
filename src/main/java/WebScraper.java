import java.io.File;
import java.util.ArrayList;

public class WebScraper {

    private WebScraperInfo info;
    private File file;
    private MarkdownWriter markdownWriter;
    private ArrayList<Link> scrapedData;

    public WebScraper(String url) {
        //TODO
    }

    public WebScraper(String url, String outputFileName) {
        //TODO
    }

    public WebScraper(String url, Language targetLanguage, String outputFileName) {
        //TODO
    }

    public WebScraper(String url, Language targetLanguage, String outputFileName, int searchDepth) {
        //TODO
    }

    public void scrape() {
        getLinks(info.getSearchDepth());
        getHeaders();
        if (shouldTranslate()) {
            translate();
        }
        writeToFile();
    }

    private void getLinks(int currentDepth) {
        //TODO
    }

    private void getHeaders() {
        //TODO
    }

    private void translate() {
        //TODO
    }

    private void writeToFile() {
        //TODO
    }

    private boolean shouldTranslate() {
        return info.getTargetLanguage() != Language.NONE;
    }
}

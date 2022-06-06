import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WebScraper {

    private WebScraperInfo info;
    private File file;
    private ArrayList<Link> links;
    private static LanguageTranslator translator;

    public WebScraper(String url, int searchDepth, String outputFileName, Language targetLanguage) {
        this.info = new WebScraperInfo();
        this.info.setInitialURL(url);
        this.info.setSearchDepth(searchDepth);
        this.info.setTargetLanguage(targetLanguage);
        this.links = new ArrayList<>();
        this.file = new File(outputFileName);
        translator = new LanguageTranslator();
    }

    public void scrape() {
        info.setStartTime(LocalDateTime.now());
        loadDocument(info.getInitialURL(), 0);
        if (info.shouldTranslate()) {
            completeTranslations();
        }
        info.setEndTime(LocalDateTime.now());
        writeToFile();
    }
    private void loadDocument(String url, int currentDepth){
        if (url.equals("")|| currentDepth > info.getSearchDepth()){
            return;
        }
        try {
            JsoupDocument document = new JsoupDocument(url);
            Link link = addLink(url, currentDepth, false);
            loadHeaders(document, link);
            loadLinks(document, currentDepth);
        }catch (IOException e) {
            System.err.println("broken URL: '" + url + "'");
            addLink(url, currentDepth, true);
        }
    }

    private void loadLinks(JsoupDocument document, int currentDepth) {
        JsoupElements elements = new JsoupElements(document, "a[href]");

        for (Element linkElement : elements.getElements()) {
            String nextUrl = linkElement.attr("abs:href");
            if (!isAlreadyScraped(nextUrl)) {
                loadDocument(nextUrl, currentDepth + 1);
            }
        }
    }

    private boolean isAlreadyScraped(String url) {
        for (Link link: links) {
            if (url.equals(link.getURL())){
                return true;
            }
        }
        return false;
    }

    private Link addLink(String url, int currentDepth, boolean isBroken){
        Link link = new Link();
        link.setURL(url);
        link.setURLDepth(currentDepth);
        link.setBrokenURL(isBroken);
        links.add(link);
        return link;
    }

    private void loadHeaders(JsoupDocument document, Link link) {
        if (link.isBrokenURL()) {
            return;
        }
        JsoupElements elements = new JsoupElements(document, "h1, h2, h3, h4, h5, h6");
        link.setHeaders(getHeadersFrom(elements.getElements(), info));
    }

    private static ArrayList<Header> getHeadersFrom(Elements elements, WebScraperInfo info){
        ArrayList<Header> headers = new ArrayList<>();
        MultiLevelIndex multiLevelIndex = new MultiLevelIndex();
        LevelCounter levelCounter = new LevelCounter(elements);
        for (Element element: elements) {
            Header header = getHeaderFrom(element, multiLevelIndex, levelCounter, info);
            multiLevelIndex = header.getMultilevelIndex();
            headers.add(header);
        }
        return headers;
    }

    private static Header getHeaderFrom(Element element, MultiLevelIndex previousIndex, LevelCounter counter, WebScraperInfo info){
        Header header = new Header();
        String text = element.text();
        if (info.shouldTranslate()) {
            Language targetLanguage = info.getTargetLanguage();
            header.setFutureTranslation(translateWithFuture(text, targetLanguage));
        }
        header.setContent(text);
        header.setLevel(LevelCounter.getHeaderLevel(element));
        header.setHeaderLevelString(previousIndex.nextIndex(counter.getIndexLevelOf(element)));
        return header;
    }

    private static CompletableFuture<DeeplTranslation> translateWithFuture(String text, Language targetLanguage) {
        return translator.translate(text, targetLanguage);
    }

    private void completeTranslations() {
        for(Link link : links) {
            for(Header header : link.getHeaders()) {
                CompletableFuture<DeeplTranslation> futureTranslation = header.getFutureTranslation();
                try {
                    DeeplTranslation translation = futureTranslation.get();
                    header.setContent(translation.getText());
                } catch(ExecutionException | InterruptedException exception) {
                    System.err.println("Could not translate header: " + header.getContent());
                    exception.printStackTrace();
                }
            }
        }
    }

    private void writeToFile() {
        try{
            MarkdownWriter.write(file, links, info);
        }catch (IOException ioException){
            throw new UncheckedIOException(ioException);
        }
    }
}

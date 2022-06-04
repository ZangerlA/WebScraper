import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    public WebScraper(String url, int searchDepth) {
        this(url, searchDepth,"default.md", Language.NONE);
    }

    public WebScraper(String url, int searchDepth, String outputFileName) {
        this(url, searchDepth, outputFileName, Language.NONE);
    }

    public WebScraper(String url, int searchDepth, Language targetLanguage) {
        this(url, searchDepth, "default.md", targetLanguage);
    }

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
        loadLinks(info.getInitialURL(), 0);
        loadHeaders();
        if (info.shouldTranslate()) {
            completeTranslations();
        }
        info.setEndTime(LocalDateTime.now());
        writeToFile();
    }

    private void loadLinks(String url, int currentDepth) {
        if (url.equals("") || currentDepth > info.getSearchDepth()){
            return;
        }
        String nextUrl = "";
        try {
            Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Elements documentLinks = document.select("a[href]");

            for (Element linkElement : documentLinks) {
                nextUrl = linkElement.attr("abs:href");
                if (!isAlreadyScraped(nextUrl)){
                    addLink(nextUrl, currentDepth, false);
                    loadLinks(url, currentDepth + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("broken URL: '" + url + "'");
            addLink(nextUrl, currentDepth, true);
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

    private void addLink(String url, int currentDepth, boolean isBroken){
        Link link = new Link();
        link.setURL(url);
        link.setURLDepth(currentDepth);
        link.setBrokenURL(isBroken);
        links.add(link);
    }

    private void loadHeaders() {
        for (Link link: links) {
            if (link.isBrokenURL()){
                return;
            }
            try {
                // This is very inefficient. Will be fixed in Phase 2.
                Document document = Jsoup.parse(new URL(link.getURL()).openStream(), "UTF-8", link.getURL());
                Elements headerElements = document.select("h1, h2, h3, h4, h5, h6");
                link.setHeaders(getHeadersFrom(headerElements, info));
            } catch (IOException e) {
                System.err.println("Headers: " + e.getMessage());
            }
        }
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

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

public class WebScraper {

    private WebScraperInfo info;
    private File file;
    private MarkdownWriter markdownWriter;
    private ArrayList<Link> links;

    public WebScraper(String url, int searchDepth) {
        this.info = new WebScraperInfo();
        this.info.setInitialURL(url);
        this.info.setSearchDepth(searchDepth);
        this.info.setSourceLanguage(Language.NONE);
        this.info.setTargetLanguage(Language.NONE);
        this.links = new ArrayList<>();
        this.file = new File("default.md");
    }

    public WebScraper(String url, String outputFileName, int searchDepth) {
        this.info = new WebScraperInfo();
        this.info.setInitialURL(url);
        this.info.setSearchDepth(searchDepth);
        this.info.setSourceLanguage(Language.NONE);
        this.info.setTargetLanguage(Language.NONE);
        this.links = new ArrayList<>();
        this.file = new File(outputFileName);
        this.markdownWriter = new MarkdownWriter(file);
    }

    public WebScraper(String url, Language targetLanguage, String outputFileName, int searchDepth) {
        this.info = new WebScraperInfo();
        this.info.setInitialURL(url);
        this.info.setSearchDepth(searchDepth);
        this.info.setSourceLanguage(Language.NONE);
        this.info.setTargetLanguage(targetLanguage);
        this.links = new ArrayList<>();
        this.file = new File(outputFileName);
        this.markdownWriter = new MarkdownWriter(file);
    }

    public void scrape() {
        info.setStartTime(LocalDateTime.now());
        getLinks(info.getInitialURL(), 0);
        loadHeaders();
        if (info.shouldTranslate()) {
            translate();
        }
        info.setEndTime(LocalDateTime.now());
        writeToFile();
    }

    private void getLinks(String url, int currentDepth) {
        if (url != "" && currentDepth <= info.getSearchDepth()) {
            String linkUrl = "";
            try {
                //Jsoup.connect(url).get();
                Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
                Elements documentLinks = document.select("a[href]");

                for (Element link : documentLinks) {
                    linkUrl = link.attr("abs:href");
                    addLink(linkUrl, currentDepth);
                }

            } catch (IOException e) {
                System.err.println("broken URL: '" + url + "'");
                Link errorLink = new Link();
                errorLink.setURL(linkUrl);
                errorLink.setBrokenURL(true);
            }
        }
    }
    private void addLink(String linkUrl, int currentDepth){
        boolean alreadyCrawled = false;
        for (Link link: links) {
            if (linkUrl.equals(link.getURL())){
                alreadyCrawled = true;
            }
        }
        if (alreadyCrawled == false){
            Link link = new Link();
            link.setURL(linkUrl);
            links.add(link);

            getLinks(linkUrl, currentDepth + 1);
        }
    }

    private void loadHeaders() {
        for (Link link: links) {
            try {
                //Jsoup.connect(link.getURL()).get();
                Document document = Jsoup.parse(new URL(link.getURL()).openStream(), "UTF-8", link.getURL());
                Elements headerElements = document.select("h1, h2, h3, h4, h5, h6");
                link.setHeaders(getHeadersFrom(headerElements));
            } catch (IOException e) {
                System.err.println("Headers: " + e.getMessage());
                link.setBrokenURL(true);
            } catch (IllegalArgumentException e){
                System.out.println("Malformed URL:" + e.getMessage());
                link.setBrokenURL(true);
            }
        }
    }
    private static ArrayList<Header> getHeadersFrom(Elements elements){
        ArrayList<Header> headers = new ArrayList<>();
        MultiLevelIndex multiLevelIndex = new MultiLevelIndex();
        LevelCounter levelCounter = new LevelCounter(elements);
        for (Element element: elements) {
            Header header = getHeaderFrom(element, multiLevelIndex, levelCounter);
            multiLevelIndex = header.getMultilevelIndex();
            headers.add(header);
        }
        return headers;
    }

    private static Header getHeaderFrom(Element element, MultiLevelIndex previousIndex, LevelCounter counter){
        Header header = new Header(
                element.text(),
                LevelCounter.getHeaderLevel(element),
                previousIndex.nextIndex(counter.getIndexLevelOf(element))
        );
        return header;
    }

    private void translate() {
        //TODO
    }

    private void writeToFile() {
        try{
            MarkdownWriter.write(file, links, info);
        }catch (IOException ioException){
            throw new UncheckedIOException(ioException);
        }
    }
}

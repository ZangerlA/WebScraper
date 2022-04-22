import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;

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
        this.markdownWriter = new MarkdownWriter(file);
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
        getHeaders();
        if (shouldTranslate()) {
            translate();
        }
        info.setEndTime(LocalDateTime.now());
        writeToFile();
    }

    private void getLinks(String url, int currentDepth) {
        if (url != "" && currentDepth <= info.getSearchDepth()) {
            String linkUrl = "";
            try {
                Document document = Jsoup.connect(url).get();
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

    private void getHeaders() {
        for (Link scrapeData: links) {
            try {
                Document document = Jsoup.connect(scrapeData.getURL()).get();
                Elements headerElements = document.select("h1, h2, h3, h4, h5, h6");

                //headelevelcounter
                int[] levelCounter = {0,0,0,0,0,0};
                int lastLevel = 0;
                int highestLevel = 0;

                for (Element headerElement: headerElements) {
                    int headerLevel = Integer.parseInt(headerElement.tagName().substring(headerElement.tagName().length() - 1));

                    // headerlevelcounter.sethighestLevel
                    for (int i = 0; i < 6; i++) {
                        if (levelCounter[i] != 0) {
                            highestLevel = i + 1;
                            break;
                        }
                    }

                    if (headerLevel == highestLevel || highestLevel == 0){
                        levelCounter[headerLevel - 1] = levelCounter[headerLevel - 1] + 1;1

                        //headerlevelcounter.clearhigherLevels
                        for (int i = headerLevel; i < levelCounter.length; i++) {
                            if (levelCounter[i] != 0){
                                levelCounter[i] = 1;
                            }
                        }

                        Header newHeader = new Header(headerElement.text(), headerLevel, getHeaderLevelString(levelCounter));
                        scrapeData.addHeader(newHeader);
                        lastLevel = headerLevel;
                    }
                    else if (levelCounter[headerLevel - 1] == headerLevel || lastLevel < headerLevel){
                        levelCounter[headerLevel - 1] += 1;
                        Header newHeader = new Header(headerElement.text(), headerLevel, getHeaderLevelString(levelCounter));
                        scrapeData.addHeader(newHeader);
                        lastLevel = headerLevel;
                    }
                    else if (lastLevel >= headerLevel){
                        //headerlevelcounter.clearhigherLevels
                        for (int i = headerLevel; i < levelCounter.length; i++) {
                            if (levelCounter[i] != 0){
                                levelCounter[i] = 1;
                            }
                        }
                        levelCounter[headerLevel - 1] += 1;
                        Header newHeader = new Header(headerElement.text(), headerLevel, getHeaderLevelString(levelCounter));
                        scrapeData.addHeader(newHeader);
                        lastLevel = headerLevel;
                    }
                }
            } catch (Exception e) {
                System.err.println("Headers: " + e.getMessage());
                scrapeData.setBrokenURL(true);
            }
        }
    }

    private String getHeaderLevelString(int[] headerLevels){
        String headerString = "";
        for (int i = 0; i < headerLevels.length; i++) {
            if (headerLevels[i] != 0){
                if (i != headerLevels.length - 1){
                    headerString += headerLevels[i] + ".";
                }
                else {
                    headerString += headerLevels[i];
                }
            }
        }
        if (headerString.endsWith(".")){
            headerString = headerString.substring(0, headerString.length() - 1);
        }
        return headerString;
    }

    private void translate() {
        //TODO
    }

    private void writeToFile() {
        try{
            markdownWriter.openFile();
            markdownWriter.writeToFile(links, info);
            markdownWriter.closeFile();
        }catch (Exception e){
            System.out.println("Couldn´t write to File: " + e.getMessage());
        }
    }

    private boolean shouldTranslate() {
        return info.getTargetLanguage() != Language.NONE;
    }
}

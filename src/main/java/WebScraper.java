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
        info.setInitialURL(url);
        info.setSearchDepth(searchDepth);
        info.setSourceLanguage(Language.NONE);
        info.setTargetLanguage(Language.NONE);
        links = new ArrayList<>();
        this.file = new File("default.md");
        this.markdownWriter = new MarkdownWriter(file);
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
            String lastLink = "";
            try {
                Document document = Jsoup.connect(url).get();
                Elements linksOnPage = document.select("a[href]");

                for (Element link : linksOnPage) {
                    lastLink = link.attr("abs:href");
                    checkDuplicateLinks(lastLink, currentDepth);
                }

            } catch (IOException e) {
                System.err.println("ForUrl broken: '" + url + "'");
                Link errorLink = new Link();
                errorLink.setURL(lastLink);
                errorLink.setBrokenURL(true);
            }
        }
    }
    private void checkDuplicateLinks(String link, int currentDepth){
        boolean alreadyCrawled = false;
        for (Link scrapData: links) {
            if (link.equals(scrapData.getURL())){
                alreadyCrawled = true;
            }
        }
        if (alreadyCrawled == false){
            Link newSD = new Link();
            newSD.setURL(link);
            links.add(newSD);

            getLinks(link, currentDepth + 1);
        }
    }

    private void getHeaders() {
        for (Link scrapeData: links) {
            try {
                Document document = Jsoup.connect(scrapeData.getURL()).get();
                Elements headerElements = document.select("h1, h2, h3, h4, h5, h6");

                int[] levelCounter = {0,0,0,0,0,0}; //counter vor every headerlvl
                int lastLevel = 0; // last level
                int highestLevel = 0;

                for (Element header: headerElements) {
                    int headerLVL = Integer.parseInt(header.tagName().substring(header.tagName().length() - 1));

                    for (int i = 0; i < 6; i++) {
                        if (levelCounter[i] != 0) {
                            highestLevel = i + 1;
                            break;
                        }
                    }

                    if (headerLVL == highestLevel || highestLevel == 0){
                        levelCounter[headerLVL - 1] = levelCounter[headerLVL - 1] + 1;
                        for (int i = headerLVL; i < levelCounter.length; i++) {
                            if (levelCounter[i] != 0){
                                levelCounter[i] = 1;
                            }
                        }
                        Header newHeader = new Header(header.text(), headerLVL, getHeaderLevelString(levelCounter));
                        scrapeData.addHeader(newHeader);
                        lastLevel = headerLVL;
                    }
                    else if (levelCounter[headerLVL - 1] == headerLVL || lastLevel < headerLVL){
                        levelCounter[headerLVL - 1] += 1;
                        Header newHeader = new Header(header.text(), headerLVL, getHeaderLevelString(levelCounter));
                        scrapeData.addHeader(newHeader);
                        lastLevel = headerLVL;
                    }
                    else if (lastLevel >= headerLVL){
                        for (int i = headerLVL; i < levelCounter.length; i++) {
                            if (levelCounter[i] != 0){
                                levelCounter[i] = 1;
                            }
                        }
                        levelCounter[headerLVL - 1] += 1;
                        Header newHeader = new Header(header.text(), headerLVL, getHeaderLevelString(levelCounter));
                        scrapeData.addHeader(newHeader);
                        lastLevel = headerLVL;
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
            if (headerLevels[i] == 0){
            }
            else if (i != headerLevels.length - 1){
                headerString += headerLevels[i] + ".";
            }
            else {
                headerString += headerLevels[i];
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
        //TODO
        try{
            markdownWriter.openFile();
            markdownWriter.writeToFile(links, info);
            markdownWriter.closeFile();
        }catch (Exception e){
            System.out.println("Couldn´t write to File!" + e.getMessage());
        }
    }

    private boolean shouldTranslate() {
        return info.getTargetLanguage() != Language.NONE;
    }
}

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WebScraper {

    private Language targetLanguage;
    private int searchDepth;
    private String startURL;
    private File file;
    private MarkdownWriter markdownWriter;
<<<<<<< Updated upstream
    private ArrayList<Link> scrapedData;
=======
    private ArrayList<ScrapeData> scrapedData = new ArrayList<>();
>>>>>>> Stashed changes

    public WebScraper(String url, int searchDepth) {
        this.startURL = url;
        this.searchDepth = searchDepth;
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
        getLinks(startURL , 0);
        getHeaders();
        if (shouldTranslate()) {
            translate();
        }
        writeToFile();
    }

    private void getLinks(String url, int currentDepth) {
        if (url != "" && currentDepth <= searchDepth) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements linksOnPage = document.select("a[href]");

                for (Element link : linksOnPage) {
                    checkDuplicateLinks(link.attr("abs:href"), currentDepth);
                }

            } catch (IOException e) {
                System.err.println("For '" + url + "': " + e.getMessage());
            }
        }
    }

    private void checkDuplicateLinks(String link, int currentDepth){
        boolean alreadyCrawled = false;
        for (ScrapeData scrapData: scrapedData) {
            if (link.equals(scrapData.getURL())){
                alreadyCrawled = true;
            }
        }
        if (alreadyCrawled == false){
            ScrapeData newSD = new ScrapeData();
            newSD.setURL(link);
            scrapedData.add(newSD);

            System.out.println(link);
            System.out.println("Current Depth:" + currentDepth);

            getLinks(link, currentDepth + 1);
        }
    }

    private void getHeaders() {
        for (ScrapeData scrapeData: scrapedData) {
            try {
                Document document = Jsoup.connect(scrapeData.getURL()).get();

                Elements headerElements = document.select("h0, h1, h2, h3, h4, h5, h6");

                System.out.println(headerElements.size());

                int headerLevelCounter = 1;
                int[] levelCounter = {1,1,1,1,1,1,1}; //counter vor every headerlvl
                int lastLevel = 0; // last level

                for (Element header: headerElements) {
                    int headerLVL = Integer.parseInt(header.tagName().substring(header.tagName().length() - 1));
                    System.out.println("Header Attribute: " + levelCounter[0] + ", "+ levelCounter[1] + ", "+ levelCounter[2] + ", "+ levelCounter[3] + ", "+ levelCounter[4] + ", " + levelCounter[5] + ", "+ levelCounter[6]);

                    if (levelCounter[headerLVL] == headerLVL || lastLevel < headerLVL){
                        System.out.println("counter up");
                        levelCounter[headerLVL]++;
                        Header newHeader = new Header(header.text(), headerLVL, levelCounter[headerLVL]);
                        scrapeData.addHeader(newHeader);
                        lastLevel = headerLVL;
                    }
                    else if (lastLevel > headerLVL){
                        levelCounter[lastLevel] = 1;
                        Header newHeader = new Header(header.text(), headerLVL, levelCounter[headerLVL]);
                        scrapeData.addHeader(newHeader);
                        lastLevel = headerLVL;
                    }
                    
                    headerLevelCounter++;
                }

            } catch (Exception e) {
                System.err.println("For '" +  "': " + e.getMessage());
            }
        }
    }

    private void translate() {
        //TODO
    }

    private void writeToFile() {
        //TODO
    }

    private boolean shouldTranslate() {
        return this.targetLanguage != Language.NONE;
    }

    public File getFile() {
        return file;
    }
}

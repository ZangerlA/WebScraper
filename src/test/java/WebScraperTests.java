import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class WebScraperTests {
    WebScraper webScraperWithUrlDepth;
    WebScraper webScraperWithUrlDepthLanguage;
    WebScraper webScraperWithUrlDepthFile;
    WebScraper webScraperWithUrlDepthLanguageFile;
    final String fileTestName = "default.md";

    @BeforeEach
    public void init() {
        webScraperWithUrlDepth = new WebScraper("quotes.toscrape.com/", 1);
        webScraperWithUrlDepthLanguage = new WebScraper("https://teamkill.at/",1, Language.DE);
        webScraperWithUrlDepthFile = new WebScraper("https://quotes.toscrape.com/",1, fileTestName);
        webScraperWithUrlDepthLanguageFile = new WebScraper("https://teamkill.at/", 1, fileTestName, Language.EN);
    }

    @AfterEach
    public void cleanup() {
        File file = new File(fileTestName);
        file.delete();
    }

    @Test
    public void WhenScrapingWithValidArgumentsThenShouldNotThrow() {
        assertDoesNotThrow(()-> webScraperWithUrlDepth.scrape());
        assertDoesNotThrow(()-> webScraperWithUrlDepthLanguage.scrape());
        assertDoesNotThrow(()-> webScraperWithUrlDepthFile.scrape());
        assertDoesNotThrow(()-> webScraperWithUrlDepthLanguageFile.scrape());
    }

    @Test
    public void WhenScrapingWithValidArgumentsThenScraperShouldFinish() {
        assertTimeout(Duration.ofMinutes(1), ()-> webScraperWithUrlDepth.scrape());
    }

    @Test
    public void WhenScrapingWithValidArgumentsThenFileShouldExist() {
        webScraperWithUrlDepthFile.scrape();
        File file = new File(fileTestName);
        assertTrue(file.exists());
    }

    @Test
    public void WhenSearchDepthNegativeThenShouldNotThrow(){
        webScraperWithUrlDepth = new WebScraper("abc", -1);
        assertDoesNotThrow(()-> webScraperWithUrlDepth.scrape());
    }
    @Test
    public void WhenInvalidFileNameThenDoesnotThrow(){
        webScraperWithUrlDepth = new WebScraper("https://quotes.toscrape.com/",1, "//.");
        assertDoesNotThrow(()-> webScraperWithUrlDepth.scrape());
    }
}

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class WebScraperTests {
    WebScraper webScraperWithUrlDepthLanguageFile;
    final String fileTestName = "default.md";

    @BeforeEach
    public void init() {
        webScraperWithUrlDepthLanguageFile = new WebScraper("https://teamkill.at/", 1, fileTestName, Language.EN);
    }

    @AfterEach
    public void cleanup() {
        File file = new File(fileTestName);
        file.delete();
    }

    @Test
    public void WhenValidArgumentsThenShouldNotThrow() {
        assertDoesNotThrow(()-> webScraperWithUrlDepthLanguageFile.scrape());
    }

    @Test
    public void WhenValidArgumentsThenScraperShouldFinish() {
        assertTimeout(Duration.ofMinutes(1), ()-> webScraperWithUrlDepthLanguageFile.scrape());
    }

    @Test
    public void WhenValidArgumentsThenFileShouldExist() {
        webScraperWithUrlDepthLanguageFile.scrape();
        File file = new File(fileTestName);
        assertTrue(file.exists());
    }

    @Test
    public void WhenSearchDepthNegativeThenShouldNotThrow(){
        webScraperWithUrlDepthLanguageFile = new WebScraper("abc", -1, fileTestName , Language.EN);
        assertDoesNotThrow(()-> webScraperWithUrlDepthLanguageFile.scrape());
    }
    @Test
    public void WhenInvalidFileNameThenDoesNotThrow(){
        webScraperWithUrlDepthLanguageFile = new WebScraper("https://quotes.toscrape.com/",1, "//.", Language.EN);
        assertDoesNotThrow(()-> webScraperWithUrlDepthLanguageFile.scrape());
    }
}

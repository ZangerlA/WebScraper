import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MarkdownWriterTests {

    File file;
    MarkdownWriter markdownWriter;
    static ArrayList<Link> standardTestLinks;
    static WebScraperInfo standardTestInfo;
    final String testFileName = "test.md";

    @BeforeAll
    static void createScraperData() {
        standardTestLinks = createLinks();
        standardTestInfo = createWebScraperInfo();
    }

    @AfterEach
    void deleteFile() throws IOException {
        markdownWriter.closeFile();
        file.delete();
    }

    @Test
    void WhenOpenFileIsCalledThenFileShouldExist() throws IOException {
        openTestFile();
        assertTrue(file.exists());
    }

    @Test
    void WhenFileNameNotValidThenExceptionShouldBeThrown() {
        setupWriter("///.");
        assertThrows(IOException.class, () -> markdownWriter.openFile());
    }

    @Test
    void WhenFileAlreadyExistsThenItShouldNotBeOverwritten() throws IOException {
        openTestFile();
        long lastModifiedBefore = file.lastModified();
        markdownWriter.closeFile();
        openTestFile();
        long lastModifiedAfter = file.lastModified();

        assertTrue(lastModifiedBefore == lastModifiedAfter);
    }

    @Test
    void WhenWritingValidLinksThenWriterShouldNotThrow() {
        assertDoesNotThrow(() -> writeTestFile());
    }

    @Test
    void WhenWritingValidLinksThenItShouldMatchComparisonFile() throws IOException {
        writeTestFile();
        markdownWriter.closeFile();

        Path comparisonFilePath = getComparisonFile();
        long mismatch = Files.mismatch(comparisonFilePath, file.toPath());

        assertEquals(-1, mismatch);
    }

    private void writeTestFile() throws IOException {
        openTestFile();
        markdownWriter.writeToFile(standardTestLinks, standardTestInfo);
    }

    private void setupWriter(String fileName) {
        file = new File(fileName);
        markdownWriter = new MarkdownWriter(file);
    }

    private void openTestFile() throws IOException {
        setupWriter(testFileName);
        markdownWriter.openFile();
    }

    private static ArrayList<Link> createLinks() {
        ArrayList<Link> links = new ArrayList<>();
        Link link1 = new Link();
        Link link2 = new Link();
        Link link3 = new Link();
        link1.setURL("www.testURL1.com");
        link2.setURL("www.testURL2.com");
        link3.setURL("www.brokenURL.com");
        link1.setURLDepth(0);
        link2.setURLDepth(1);
        link3.setURLDepth(1);
        link1.setBrokenURL(false);
        link2.setBrokenURL(false);
        link3.setBrokenURL(true);
        link1.addHeader(new Header("link1test1", 1, "1"));
        link1.addHeader(new Header("link1test2", 2, "1.1"));
        link2.addHeader(new Header("link2test1", 1, "1"));
        link2.addHeader(new Header("link2test2", 1, "2"));
        links.add(link1);
        links.add(link2);
        links.add(link3);

        return links;
    }

    private static WebScraperInfo createWebScraperInfo() {
        WebScraperInfo info = new WebScraperInfo();
        info.setInitialURL("www.testURL1.com");
        info.setSourceLanguage(Language.EN);
        info.setTargetLanguage(Language.DE);
        info.setSearchDepth(1);
        info.setStartTime(LocalDateTime.of(2022, Month.DECEMBER, 24, 20, 00, 00));
        info.setEndTime(LocalDateTime.of(2022, Month.DECEMBER, 24, 20, 02, 00));

        return info;
    }

    private static Path getComparisonFile() throws IOException {
        String comparisonFileAsString = getComparisonFileAsString();
        Path comparisonFilePath = Files.createTempFile("comparisonFile", ".md");
        Files.writeString(comparisonFilePath, comparisonFileAsString);

        return comparisonFilePath;
    }

    private static String getComparisonFileAsString() {
        String newLine = System.lineSeparator();
        String fileAsString =
                "> INFORMATION:" + newLine +
                        "<br>input: www.testURL1.com" + newLine +
                        "<br>depth: 1" + newLine +
                        "<br>source language: EN" + newLine +
                        "<br>target language: DE" + newLine +
                        "<br>start time: 24-12-2022 20:00:00" + newLine +
                        "<br>end time: 24-12-2022 20:02:00" + newLine +
                        newLine +
                        "# <br>link to <a>www.testURL1.com</a>" + newLine +
                        "## link1test1 1" + newLine +
                        "### link1test2 1.1" + newLine +
                        newLine +
                        "# <br>--> link to <a>www.testURL2.com</a>" + newLine +
                        "## link2test1 1" + newLine +
                        "## link2test2 2" + newLine +
                        newLine +
                        "# <br>--> broken link <a>www.brokenURL.com</a>" + newLine +
                        newLine;

        return fileAsString;
    }
}

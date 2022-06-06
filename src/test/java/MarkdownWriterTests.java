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
    static ArrayList<Link> standardTestLinks;
    static WebScraperInfo standardTestInfo;
    final String testFileName = "test.md";

    @BeforeAll
    static void createScraperData() {
        standardTestLinks = createLinks();
        standardTestInfo = createWebScraperInfo();
    }

    @AfterEach
    void deleteFile() {
        file.delete();
    }

    @Test
    void WhenWritingFileThenFileShouldExist() throws IOException {
        writeTestFile();
        assertTrue(file.exists());
    }

    @Test
    void WhenFileNameNotValidThenFallbackNameShouldBeChosen() throws IOException {
        createFile("///.");
        MarkdownWriter.write(file, standardTestLinks, standardTestInfo);
        assertEquals("default.md", file.getName());
    }

    @Test
    void WhenFileAlreadyExistsThenWritingShouldAppend() throws IOException {
        writeTestFile();
        long previousLength = file.length();
        writeTestFile();
        long length = file.length();

        assertEquals(previousLength * 2, length);
    }

    @Test
    void WhenWritingValidLinksThenWriterShouldNotThrow() {
        assertDoesNotThrow(this::writeTestFile);
    }

    @Test
    void WhenWritingTestFileThenItShouldMatchComparisonFile() throws IOException {
        writeTestFile();
        Path comparisonFilePath = getComparisonFile();
        long mismatch = Files.mismatch(comparisonFilePath, file.toPath());
        assertEquals(-1, mismatch);
    }

    @Test
    void WhenWritingValidLinksThenFileShouldNotBeEmpty() throws IOException {
        writeTestFile();
        assertNotEquals(0, file.length());
    }

    @Test
    void WhenWritingFileThenFileShouldNotBeHidden() throws IOException {
        writeTestFile();
        assertFalse(file.isHidden());
    }

    private void writeTestFile() throws IOException {
        createFile(testFileName);
        MarkdownWriter.write(file, standardTestLinks, standardTestInfo);
    }

    private void createFile(String name) {
        file = new File(name);
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
        link1.addHeader(new Header("link1test1", 1, new MultiLevelIndex(new int[]{1})));
        link1.addHeader(new Header("link1test2", 2, new MultiLevelIndex(new int[]{1, 1})));
        link2.addHeader(new Header("link2test1", 1, new MultiLevelIndex(new int[]{1})));
        link2.addHeader(new Header("link2test2", 1, new MultiLevelIndex(new int[]{2})));
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
                        "<br> input: www.testURL1.com" + newLine +
                        "<br> depth: 1" + newLine +
                        "<br> target language: DE" + newLine +
                        "<br> start time: 24-12-2022 20:00:00" + newLine +
                        "<br> end time: 24-12-2022 20:02:00" + newLine +
                        newLine +
                        "# <br> link to <a> www.testURL1.com </a>" + newLine +
                        "## link1test1 1" + newLine +
                        "### link1test2 1.1" + newLine +
                        newLine +
                        "# <br> --> link to <a> www.testURL2.com </a>" + newLine +
                        "## link2test1 1" + newLine +
                        "## link2test2 2" + newLine +
                        newLine +
                        "# <br> --> broken link <a> www.brokenURL.com </a>" + newLine +
                        newLine;

        return fileAsString;
    }
}

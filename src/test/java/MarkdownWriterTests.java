import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MarkdownWriterTests {

    File file;
    MarkdownWriter markdownWriter;
    final String testFileName = "test.md";

    @AfterEach
    void deleteFile() throws IOException {
        markdownWriter.closeFile();
        file.delete();
    }

    @Test
    void WhenOpenFileIsCalledThenFileShouldExist() throws IOException {
        setupWriter(testFileName);
        markdownWriter.openFile();
        assertTrue(file.exists());
    }

    @Test
    void WhenFileNameNotValidThenExceptionShouldBeThrown() throws IOException {
        setupWriter("///.");
        assertThrows(IOException.class, () -> markdownWriter.openFile());
    }

    @Test
    void WhenFileAlreadyExistsThenItShouldNotBeOverwritten() throws IOException {
        setupWriter(testFileName);
        markdownWriter.openFile();
        long lastModifiedBefore = file.lastModified();
        markdownWriter.closeFile();
        markdownWriter.openFile();
        long lastModifiedAfter = file.lastModified();
        assertTrue(lastModifiedBefore == lastModifiedAfter);
    }

    @Test
    void WhenWritingValidLinksThenWriterShouldNotThrow() throws IOException {
        setupWriter(testFileName);
        markdownWriter.openFile();
        ArrayList<Link> links = createLinks();
        assertDoesNotThrow(() -> markdownWriter.writeToFile(links));
        markdownWriter.closeFile();
    }

    private void setupWriter(String fileName) {
        file = new File(fileName);
        markdownWriter = new MarkdownWriter(file);
    }

    private ArrayList<Link> createLinks() {
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
        link1.addHeader(new Header("link1test1", 1));
        link1.addHeader(new Header("link1test2", 2));
        link2.addHeader(new Header("link2test1", 1));
        link2.addHeader(new Header("link2test2", 1));
        links.add(link1);
        links.add(link2);
        links.add(link3);

        return links;
    }
}

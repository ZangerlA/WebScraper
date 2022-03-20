import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MarkdownWriter {

    private FileWriter fileWriter;

    public MarkdownWriter(File file) throws IOException {
        fileWriter = new FileWriter(file);
    }
}

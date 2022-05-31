import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;

public class MarkdownWriter {

	private static File markdownFile;
	private Writer fileWriter;

	private MarkdownWriter(File file) {
		markdownFile = file;
	}

	public static synchronized File write(File file, List<Link> links, WebScraperInfo info) throws IOException {
		MarkdownWriter writer = new MarkdownWriter(file);
		writer.openFile();
		writer.writeToFile(links, info);
		writer.closeFile();
		return markdownFile;
	}

	private void openFile() throws IOException {
		checkFileIsValid();
		boolean shouldAppend = !markdownFile.createNewFile();
		try {
			fileWriter = createUTF8Writer(shouldAppend);
		} catch(IOException e) {
			throw new IOException("Could not open file.", e);
		}
	}

	public void writeToFile(List<Link> links, WebScraperInfo info) throws IOException {
		try{
			writeInfoBlockToFile(info);
			writeLinksToFile(links);
			printFilePath();
		} catch(IOException e) {
			throw new IOException("Error writing to file.", e);
		} finally {
			closeFile();
		}
	}

	private void writeInfoBlockToFile(WebScraperInfo info) throws IOException {
		String markdownInfoBlock = getMarkdownForInfoBlock(info);
		fileWriter.write(markdownInfoBlock);
		writeNewLine(1);
	}

	private void writeLinksToFile(List<Link> links) throws IOException {
		for (Link link : links) {
			String markdownLink = getMarkdownForLink(link);
			fileWriter.write(markdownLink);
			writeNewLine(1);
			writeHeadersToFile(link);
			writeNewLine(1);
		}
	}

	private void writeHeadersToFile(Link link) throws IOException {
		for (Header header : link.getHeaders()) {
			String markdownHeader = getMarkdownForHeader(header);
			fileWriter.write(markdownHeader);
			writeNewLine(1);
		}
	}

	private void printFilePath() {
		System.out.println("Scrape result successfully written to: " + markdownFile.toURI());
	}

	private String getMarkdownForInfoBlock(WebScraperInfo info) {
		StringBuilder infoBlock = new StringBuilder();
		String newLine = getNewLine();
		infoBlock
			.append("> INFORMATION:").append(newLine)
			.append("<br> input: ").append(info.getInitialURL()).append(newLine)
			.append("<br> depth: ").append(info.getSearchDepth()).append(newLine);
		if (info.shouldTranslate()) {
			infoBlock.append("<br> target language: ").append(info.getTargetLanguage().toString()).append(newLine);
		}
		infoBlock
			.append("<br> start time: ").append(info.getStartTime()).append(newLine)
			.append("<br> end time: ").append(info.getEndTime()).append(newLine);

		return infoBlock.toString();
	}

	private String getMarkdownForLink(Link links) {
		StringBuilder markdownLink = new StringBuilder();
		String URL = links.getURL();
		int URLDepth = links.getURLDepth();
		boolean isBrokenURL = links.isBrokenURL();

		appendLinkIndentation(markdownLink, URLDepth);
		appendLinkIsBroken(markdownLink, isBrokenURL);
		appendURL(markdownLink, URL);

		return markdownLink.toString();
	}

	private String getMarkdownForHeader(Header header) {
		StringBuilder markdownHeader = new StringBuilder();
		String headerText = header.getContent();
		int headerLevel = header.getLevel();
		String headerNumerator = header.getHeaderLevelString();

		appendHeaderIndentation(markdownHeader, headerLevel);
		appendHeader(markdownHeader, headerText);
		appendHeaderMultipleLevelIndex(markdownHeader, headerNumerator);

		return markdownHeader.toString();
	}

	private void appendLinkIndentation(StringBuilder markdownLink, int URLDepth) {
		markdownLink.append("# <br> ");
		for (int i = 0; i < URLDepth; i++) {
			markdownLink.append("--");
		}
		if (URLDepth != 0) {
			markdownLink.append("> ");
		}
	}

	private void appendLinkIsBroken(StringBuilder markdownLink, boolean isBrokenURL) {
		if (isBrokenURL) {
			markdownLink.append("broken link ");
		} else {
			markdownLink.append("link to ");
		}
	}

	private void appendURL(StringBuilder markdownLink, String URL) {
		markdownLink.append("<a> ");
		markdownLink.append(URL);
		markdownLink.append(" </a>");
	}

	private void appendHeaderIndentation(StringBuilder markdownHeader, int headerLevel) {
		markdownHeader.append("#");
		for (int i = 0; i < headerLevel; i++) {
			markdownHeader.append("#");
		}
		markdownHeader.append(" ");
	}

	private void appendHeader(StringBuilder markdownHeader, String headerText) {
		markdownHeader.append(headerText);
	}

	private void appendHeaderMultipleLevelIndex(StringBuilder markdownHeader, String numerator) {
		markdownHeader.append(" ").append(numerator);
	}

	private void writeNewLine(int amount) throws IOException {
		for (int i = 0; i < amount; i++) {
			fileWriter.write(getNewLine());
		}
	}

	private String getNewLine() {
		return System.lineSeparator();
	}

	private void closeFile() throws IOException {
		if (!markdownFile.exists()) {
			return;
		}
		try {
			fileWriter.close();
		} catch(IOException e) {
			throw new IOException("Could not close FileWriter.", e);
		}
	}

	private Writer createUTF8Writer(boolean shouldAppend) throws FileNotFoundException {
		return new OutputStreamWriter(new FileOutputStream(markdownFile, shouldAppend), StandardCharsets.UTF_8);
	}

	private void checkFileIsValid() {
		try {
			Paths.get(markdownFile.getPath());
		} catch(InvalidPathException invalidPathException) {
			markdownFile = new File("default.md");
			System.err.println("Given File name not valid. Using \"default.md\" as fallback.");
		}
	}
}

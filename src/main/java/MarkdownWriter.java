import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MarkdownWriter {

	private final File markdownFile;
	private FileWriter fileWriter;

	private MarkdownWriter(File file) {
		markdownFile = file;
	}

	public static MarkdownWriter getMarkdownWriterFor(File file) throws IOException {
		MarkdownWriter writer = new MarkdownWriter(file);
		writer.openFile();
		return writer;
	}

	private void openFile() throws IOException {
		boolean shouldAppend = markdownFile.createNewFile();
		fileWriter = new FileWriter(markdownFile, shouldAppend);
	}

	public void writeToFile(List<Link> links, WebScraperInfo info) throws IOException {
		writeInfoBlockToFile(info);
		writeLinksToFile(links);
		printFilePath();
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
			.append("<br>input: ").append(info.getInitialURL()).append(newLine)
			.append("<br>depth: ").append(info.getSearchDepth()).append(newLine);
		if (info.shouldTranslate()) {
			infoBlock
				.append("<br>source language: ").append(info.getSourceLanguage().toString()).append(newLine)
				.append("<br>target language: ").append(info.getTargetLanguage().toString()).append(newLine);
		}
		infoBlock
			.append("<br>start time: ").append(info.getStartTime()).append(newLine)
			.append("<br>end time: ").append(info.getEndTime()).append(newLine);

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
		markdownLink.append("# <br>");
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
		markdownLink.append("<a>");
		markdownLink.append(URL);
		markdownLink.append("</a>");
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

	public void closeFile() throws IOException {
		if (markdownFile.exists()) {
			fileWriter.close();
		}
	}
}

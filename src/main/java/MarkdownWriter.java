import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MarkdownWriter {

	private File file;
	private FileWriter fileWriter;

	public MarkdownWriter(File file) {
		this.file = file;
	}

	public void openFile() throws IOException {
		boolean appendMode = false;
		if (file.exists()) {
			appendMode = true;
		} else {
			file.createNewFile();
		}
		fileWriter = new FileWriter(this.file, appendMode);
	}

	public void writeToFile(List<Link> links, WebScraperInfo info) throws IOException {
		checkCorrectSetup(links, info);
		writeInfoBlockToFile(info);
		writeLinksAndHeadersToFile(links);
		printFilePath();
	}

	private void writeInfoBlockToFile(WebScraperInfo info) throws IOException {
		fileWriter.write(buildMarkdownForInfoBlock(info));
		writeNewLine(1);
	}

	private void writeLinksAndHeadersToFile(List<Link> links) throws IOException {
		for (Link link : links) {
			fileWriter.write(buildMarkdownForLink(link));
			writeNewLine(1);
			for (Header header : link.getHeaders()) {
				fileWriter.write(buildMarkdownForHeader(header));
				writeNewLine(1);
			}
			writeNewLine(1);
		}
	}

	private void printFilePath() {
		System.out.println("Scrape result successfully written to: " + file.toURI());
	}

	private void checkCorrectSetup(List<Link> links, WebScraperInfo info) throws IOException {
		if (fileWriter == null) {
			throw new IOException("openFile() must be called before writing to File.");
		}
		if (links == null) {
			throw new NullPointerException("links cannot be null.");
		}
		if (info == null) {
			throw new NullPointerException("Webscraper info cannot be null.");
		}
	}

	private String buildMarkdownForInfoBlock(WebScraperInfo info) {
		StringBuilder infoBlock = new StringBuilder();
		String newLine = getNewLine();
		infoBlock.append("> INFORMATION:" + newLine);
		infoBlock.append("<br>input: " + info.getInitialURL() + newLine);
		infoBlock.append("<br>depth: " + info.getSearchDepth() + newLine);
		if (info.shouldTranslate()) {
			infoBlock.append("<br>source language: " + info.getSourceLanguage().toString() + newLine);
			infoBlock.append("<br>target language: " + info.getTargetLanguage().toString() + newLine);
		}
		infoBlock.append("<br>start time: " + info.getStartTime() + newLine);
		infoBlock.append("<br>end time: " + info.getEndTime() + newLine);

		return infoBlock.toString();
	}

	private String buildMarkdownForLink(Link links) {
		StringBuilder markdownLink = new StringBuilder();
		String URL = links.getURL();
		int URLDepth = links.getURLDepth();
		boolean isBrokenURL = links.isBrokenURL();

		appendLinkIndentation(markdownLink, URLDepth);
		appendLinkIsBroken(markdownLink, isBrokenURL);
		appendLinkURL(markdownLink, URL);

		return markdownLink.toString();
	}

	private String buildMarkdownForHeader(Header header) {
		StringBuilder markdownHeader = new StringBuilder();
		String headerText = header.getHeader();
		int headerLevel = header.getHeaderLevel();
		String headerNumerator = header.getHeaderLevelString();

		appendHeaderIndentation(markdownHeader, headerLevel);
		appendHeader(markdownHeader, headerText);
		appendHeaderNumerator(markdownHeader, headerNumerator);

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

	private void appendLinkURL(StringBuilder markdownLink, String URL) {
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

	private void appendHeaderNumerator(StringBuilder markdownHeader, String numerator) {
		markdownHeader.append(" " + numerator);
	}

	private void writeNewLine(int amount) {
		try {
			for (int i = 0; i < amount; i++) {
				fileWriter.write(getNewLine());
			}
		} catch (IOException ioException) {
			System.err.println("Error writing empty-line in file.");
			ioException.printStackTrace();
		}
	}

	private String getNewLine() {
		return System.lineSeparator();
	}

	public void closeFile() throws IOException {
		if (fileWriter == null) {
			return;
		}
		if (file.exists()) {
			fileWriter.close();
		}
	}

}

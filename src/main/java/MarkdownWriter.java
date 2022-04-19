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

	public void writeToFile(List<Link> scrapeData, WebScraperInfo info) throws IOException {
		fileWriter.write(buildInfoBlock(info));
		writeNewLine(1);
		for (Link data : scrapeData) {
			fileWriter.write(buildMarkdownFromLink(data));
			writeNewLine(1);
			for (Header header : data.getHeaders()) {
				fileWriter.write(buildMarkdownFromHeader(header));
				writeNewLine(1);
			}
			writeNewLine(1);
		}
		System.out.println("Scrape result successfully written to: " + file.toURI());
	}

	private String buildInfoBlock(WebScraperInfo info) {
		StringBuilder infoBlock = new StringBuilder();
		infoBlock.append("> INFORMATION:");
		infoBlock.append("<br>input: " + info.getInitialURL());
		infoBlock.append("<br>depth: " + info.getSearchDepth());
		infoBlock.append("<br>source language: " + info.getSourceLanguage());
		infoBlock.append("<br>target language: " + info.getTargetLanguage());
		infoBlock.append("<br>start time: " + info.getStartTime());
		infoBlock.append("<br>end time: " + info.getEndTime());

		return infoBlock.toString();
	}

	private String buildMarkdownFromLink(Link links) {
		StringBuilder markdownLink = new StringBuilder();
		String URL = links.getURL();
		int URLDepth = links.getURLDepth();
		boolean isBrokenURL = links.isBrokenURL();

		appendLinkIndentation(markdownLink, URLDepth);
		appendLinkIsBrokenURL(markdownLink, isBrokenURL);
		appendURL(markdownLink, URL);

		return markdownLink.toString();
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

	private void appendLinkIsBrokenURL(StringBuilder markdownLink, boolean isBrokenURL) {
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

	private String buildMarkdownFromHeader(Header header) {
		StringBuilder markdownHeader = new StringBuilder();
		String headerText = header.getHeader();
		int headerLevel = header.getHeaderLevel();

		appendHeaderIndentation(markdownHeader, headerLevel);
		appendHeader(markdownHeader, headerText);

		return markdownHeader.toString();
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
		markdownHeader.append(numerator);
	}

	private void writeNewLine(int amount) {
		try {
			for (int i = 0; i < amount; i++) {
				fileWriter.write(System.lineSeparator());
			}
		} catch (IOException ioException) {
			System.err.println("Error writing empty-line in file.");
			ioException.printStackTrace();
		}
	}

	public void closeFile() throws IOException {
		if (file.exists()) {
			fileWriter.close();
		}
	}

}

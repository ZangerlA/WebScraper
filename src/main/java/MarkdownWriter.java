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
		if (this.file.exists()) {
			appendMode = true;
		} else {
			this.file.createNewFile();
		}
		fileWriter = new FileWriter(this.file, appendMode);
	}

	public void writeToFile(List<ScrapeData> scrapeData) throws IOException {
		for (ScrapeData data : scrapeData) {
			fileWriter.write(buildMarkdownFromLink(data));
			for (Header header : data.getHeaders()) {
				fileWriter.write(buildMarkdownFromHeader(header));
			}
			writeEmptyLine();
		}
		System.out.println("Scrape result successfully written to: " + file.toURI());
	}

	private String buildMarkdownFromLink(ScrapeData data) {
		StringBuilder markdownLink = new StringBuilder();
		String URL = data.getURL();
		int URLDepth = data.getURLDepth();
		boolean isBrokenURL = data.isBrokenURL();

		appendLinkIndentation(markdownLink, URLDepth);
		appendLinkIsBrokenURL(markdownLink, isBrokenURL);
		appendURL(markdownLink, URL);
		writeEmptyLine();

		return markdownLink.toString();
	}

	private void appendLinkIndentation(StringBuilder markdownLink, int URLDepth) {
		markdownLink.append("# <br>");
		for (int i = 0; i < URLDepth; i++) {
			markdownLink.append("--");
		}
		markdownLink.append("> ");
	}

	private void appendLinkIsBrokenURL(StringBuilder markdownLink, boolean isBrokenURL) {
		if (isBrokenURL) {
			markdownLink.append("broken link");
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

	private void writeEmptyLine() {
		try {
			fileWriter.write("\n\n");
		} catch (IOException ioException) {
			System.err.println("Error writing empty-line in file.");
			ioException.printStackTrace();
		}
	}

	public void closeFile() throws IOException {
		fileWriter.close();
	}

}

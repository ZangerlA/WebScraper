import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WebScraperInfo {
	private String initialURL;
	private Language sourceLanguage;
	private Language targetLanguage;
	private int searchDepth;

	private String startTime;
	private String endTime;

	public WebScraperInfo() {
	}

	public String getInitialURL() {
		return initialURL;
	}

	public void setInitialURL(String initialURL) {
		this.initialURL = initialURL;
	}

	public Language getSourceLanguage() {
		return sourceLanguage;
	}

	public void setSourceLanguage(Language sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public Language getTargetLanguage() {
		return targetLanguage;
	}

	public void setTargetLanguage(Language targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	public boolean shouldTranslate() {
		if (targetLanguage == Language.NONE) {
			return false;
		}
		return true;
	}

	public int getSearchDepth() {
		return searchDepth;
	}

	public void setSearchDepth(int searchDepth) {
		this.searchDepth = searchDepth;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = formatTime(startTime);
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = formatTime(endTime);
	}

	private String formatTime(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return time.format(formatter);
	}
}

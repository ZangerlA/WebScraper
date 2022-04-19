import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WebScraperInfo {
	private String initialURL;
	private Language sourceLanguage;
	private Language targetLanguage;
	private int searchDepth;

	private String startTime;
	private String endTime;

	public WebScraperInfo(String initialURL, Language sourceLanguage, Language targetLanguage, int searchDepth) {
		this.initialURL = initialURL;
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage;
		this.searchDepth = searchDepth;
	}

	public String getInitialURL() {
		return initialURL;
	}

	public Language getSourceLanguage() {
		return sourceLanguage;
	}

	public Language getTargetLanguage() {
		return targetLanguage;
	}

	public int getSearchDepth() {
		return searchDepth;
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

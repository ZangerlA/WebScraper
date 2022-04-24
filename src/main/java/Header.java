import java.util.concurrent.CompletableFuture;

public class Header {
    private String content;
    private int level;
    private MultiLevelIndex headerLevelString;
    private CompletableFuture<DeeplTranslation> futureTranslation;

    public Header() {

    }

    public Header(String header, int headerLevel, MultiLevelIndex numInLevel) {
        this.content = header;
        this.level = headerLevel;
        this.headerLevelString = numInLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getHeaderLevelString() {
        return headerLevelString.toString();
    }

    public MultiLevelIndex getMultilevelIndex(){
        return this.headerLevelString;
    }

    public void setHeaderLevelString(MultiLevelIndex headerLevelString) {
        this.headerLevelString = headerLevelString;
    }

    public CompletableFuture<DeeplTranslation> getFutureTranslation() {
        return futureTranslation;
    }

    public void setFutureTranslation(CompletableFuture<DeeplTranslation> futureTranslation) {
        this.futureTranslation = futureTranslation;
    }
}

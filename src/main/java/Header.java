public class Header {
    private String content;
    private int level;
    private String headerLevelString;

    public Header(String header, int headerLevel, String numInLevel) {
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

    public String  getHeaderLevelString() {
        return headerLevelString;
    }

    public void setHeaderLevelString(String headerLevelString) {
        this.headerLevelString = headerLevelString;
    }
}

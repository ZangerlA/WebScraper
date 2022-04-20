import java.util.stream.Stream;

public class Header {
    private String header;
    private int headerLevel;

    private String headerLevelString;

    private int numInLevel;

    public Header(String header, int headerLevel, String numInLevel) {
        this.header = header;
        this.headerLevel = headerLevel;
        this.headerLevelString = numInLevel;
    }

    public Header(String header, int headerLevel, int numInLevel) {
        this.header = header;
        this.headerLevel = headerLevel;
        this.numInLevel = numInLevel;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getHeaderLevel() {
        return headerLevel;
    }

    public void setHeaderLevel(int headerLevel) {
        this.headerLevel = headerLevel;
    }

    public String  getHeaderLevelString() {
        return headerLevelString;
    }

    public void setHeaderLevelString(String headerLevelString) {
        this.headerLevelString = headerLevelString;
    }
}

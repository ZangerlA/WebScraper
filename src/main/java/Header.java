public class Header {
    private String header;
    private int headerLevel;

    public Header(String header, int headerLevel) {
        this.header = header;
        this.headerLevel = headerLevel;
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
}

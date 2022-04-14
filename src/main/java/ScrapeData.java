import java.util.ArrayList;

public class ScrapeData {
    private String URL;
    private int URLDepth;
    private ArrayList<Header> headers;

    public ScrapeData() {
        headers = new ArrayList<>();
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getURLDepth() {
        return URLDepth;
    }

    public void setURLDepth(int URLDepth) {
        this.URLDepth = URLDepth;
    }

    public ArrayList<Header> getHeaders() {
        ArrayList<Header> headersCopy = new ArrayList<>();
        for (Header header : this.headers) {
            headersCopy.add(header);
        }
        return headersCopy;
    }

    public void addHeader(Header header) {
        headers.add(header);
    }

}

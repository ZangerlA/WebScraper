import java.util.ArrayList;

public class Link {
	private String URL;
	private int URLDepth;
	private boolean isBrokenURL;
	private ArrayList<Header> headers;

	public Link() {
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
		return new ArrayList<>(this.headers);
	}

	public void setHeaders(ArrayList<Header> headers) {
		this.headers = new ArrayList<>(headers);
	}
	public boolean isBrokenURL() {
		return isBrokenURL;
	}

	public void setBrokenURL(boolean brokenURL) {
		isBrokenURL = brokenURL;
	}

	public void addHeader(Header header) {
		headers.add(header);
	}

}

public class Main {
    public static void main(String[] args) {
        WebScraper webScraper = new WebScraper("https://quotes.toscrape.com/", 1);
        webScraper.scrape();
    }
}

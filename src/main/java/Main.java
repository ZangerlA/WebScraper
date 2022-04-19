public class Main {
    public static void main(String[] args) {
        WebScraper webScraper = new WebScraper("https://www.hypovereinsbank.de/hvb/privatkunden", 1);
        webScraper.scrape();
    }
}

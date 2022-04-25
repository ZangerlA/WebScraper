public class Main {
    //Testurl= "https://quotes.toscrape.com/"
    public static void main(String[] args) {
        userConsoleInputScraper();
    }

    public static void userConsoleInputScraper() {
        ConsoleUserInput consoleUserInput = new ConsoleUserInput();
        if (!consoleUserInput.getSaveFilePath().equals("") && consoleUserInput.getTargetLanguage() != null) {
            WebScraper webScraper = new WebScraper(consoleUserInput.getUrl(), consoleUserInput.getSearchDepth(), consoleUserInput.getSaveFilePath(), consoleUserInput.getTargetLanguage());
            webScraper.scrape();
        } else if (!consoleUserInput.getSaveFilePath().equals("") && consoleUserInput.getTargetLanguage() == null) {
            WebScraper webScraper = new WebScraper(consoleUserInput.getUrl(), consoleUserInput.getSearchDepth(), consoleUserInput.getSaveFilePath());
            webScraper.scrape();
        } else if (consoleUserInput.getSaveFilePath().equals("") && consoleUserInput.getTargetLanguage() != null) {
            WebScraper webScraper = new WebScraper(consoleUserInput.getUrl(), consoleUserInput.getSearchDepth(), consoleUserInput.getTargetLanguage());
            webScraper.scrape();
        } else {
            WebScraper webScraper = new WebScraper(consoleUserInput.getUrl(), consoleUserInput.getSearchDepth());
            webScraper.scrape();
        }
    }
}

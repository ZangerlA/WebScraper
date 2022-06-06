
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    //Testurl= "https://quotes.toscrape.com/"
    public static void main(String[] args) {

        userConsoleInputScraper();
    }

    public static void userConsoleInputScraper() {
        ConsoleUserInput consoleUserInput = new ConsoleUserInput();
        ExecutorService service = Executors.newCachedThreadPool();

        for(String url : consoleUserInput.getUrls()) {
            Runnable task = () -> {
                WebScraper webScraper = new WebScraper(url, consoleUserInput.getSearchDepth(), consoleUserInput.getSaveFilePath(), consoleUserInput.getTargetLanguage());
                webScraper.scrape();
            };
            service.execute(task);
        }
        service.shutdown();
    }
}

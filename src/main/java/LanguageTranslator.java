import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class LanguageTranslator {

    private final String DEEPL_TOKEN = System.getenv("DEEPL_TOKEN");
    private HttpClient client;
    private HttpRequest request;

    public LanguageTranslator() {
        client = HttpClient.newHttpClient();
    }

    public void translate(String text, Language targetLanguage) {

    }



}

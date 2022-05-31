import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

public class TranslationRequestBuilder {

    private static final String DEEPL_API = "https://api-free.deepl.com/v2/translate?";
    private static final String DEEPL_TOKEN = getDeepLTokenFromSystem();

    private TranslationRequestBuilder() {
    }

    public static HttpRequest buildRequest(String text, Language targetLanguage) {
        text = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String authKeyURI = "auth_key=" + DEEPL_TOKEN;
        String textURI = "&text=" + text;
        String targetLanguageURI = "&target_lang=" + targetLanguage.toISO639_1();

        URI deeplAPI = URI.create(DEEPL_API + authKeyURI + textURI + targetLanguageURI);
        return HttpRequest.newBuilder(deeplAPI).build();
    }

    private static String getDeepLTokenFromSystem() {
        String token = System.getenv("DEEPL_TOKEN");
        if(token == null) {
            throw new RuntimeException("Could not read DEEPL_TOKEN from Environment. Please check if set correctly.");
        }
        return token;
    }
}

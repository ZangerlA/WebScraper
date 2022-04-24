import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class LanguageTranslator {

    private static final String DEEPL_API = "https://api-free.deepl.com/v2/translate?";
    private static String DEEPL_TOKEN;
    private HttpClient client;

    public LanguageTranslator() {
        client = HttpClient.newHttpClient();
        DEEPL_TOKEN = getDeepLTokenFromSystem();
    }

    public CompletableFuture<DeeplTranslation> translate(String text, Language targetLanguage) {
        HttpRequest request = buildRequest(text, targetLanguage);
        CompletableFuture<HttpResponse<String>> result;
        result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return result.thenApply(LanguageTranslator::parse);
    }

    private static HttpRequest buildRequest(String text, Language targetLanguage) {
        text = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String authKeyURI = "auth_key=" + DEEPL_TOKEN;
        String textURI = "&text=" + text;
        String targetLanguageURI = "&target_lang=" + targetLanguage.toISO639_1();

        URI deeplAPI = URI.create(DEEPL_API + authKeyURI + textURI + targetLanguageURI);
        return HttpRequest.newBuilder(deeplAPI).build();
    }

    private static DeeplTranslation parse(HttpResponse<String> response) {
        String jsonBody = getBodyFromResponse(response);
        return parseJson(jsonBody);
    }

    private static String getBodyFromResponse(HttpResponse<String> response) {
        return response.body();
    }

    private static DeeplTranslation parseJson(String jsonBody) {
        ObjectMapper mapper = new ObjectMapper();
        DeeplTranslation translation;

        try {
            DeeplResponse response =  mapper.readValue(jsonBody, DeeplResponse.class);
            translation = response.getTranslations().get(0);
        }catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
            translation = new DeeplTranslation();
            translation.setText("translation error");
            translation.setDetected_source_language("translation error");
        }
        return translation;
    }

    private static String getDeepLTokenFromSystem() {
        String token = System.getenv("DEEPL_TOKEN");
        if(token == null) {
            throw new RuntimeException("Could not read DEEPL_TOKEN from Environment. Please check if set correctly.");
        }
        return token;
    }
}
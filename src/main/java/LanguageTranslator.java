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

    private HttpClient client;

    public LanguageTranslator() {
        client = HttpClient.newHttpClient();
    }

    public CompletableFuture<DeeplTranslation> translate(String text, Language targetLanguage) {
        HttpRequest request = TranslationRequestBuilder.buildRequest(text, targetLanguage);
        CompletableFuture<HttpResponse<String>> result;
        result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return result.thenApply((response) -> LanguageTranslator.parseJson(response, text));
    }

    private static DeeplTranslation parseJson(HttpResponse<String> httpResponse, String text) {
        JsonParser jsonParser = new JsonParser();
        DeeplTranslation translation;

        try {
            DeeplResponse deeplResponse =  jsonParser.parse(httpResponse.body(), DeeplResponse.class);
            translation = deeplResponse.getElementAt(0);
        }catch (JsonProcessingException e) {
            translation = getErrorTranslationFor(text);
        }
        return translation;
    }

    private static DeeplTranslation getErrorTranslationFor(String text) {
        DeeplTranslation translation = new DeeplTranslation();
        translation.setText(text + " (could not translate)");
        translation.setDetected_source_language("translation error");
        return translation;
    }
}
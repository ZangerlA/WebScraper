import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LanguageTranslator {

    private static final String DEEPL_API = "https://api-free.deepl.com/v2/translate?";
    private static final String DEEPL_TOKEN = System.getenv("DEEPL_TOKEN");
    private HttpClient client;

    public LanguageTranslator() {
        client = HttpClient.newHttpClient();
    }

    public CompletableFuture<DeeplTranslation> translate(String text, Language targetLanguage) {
        HttpRequest request = buildRequest(text, targetLanguage);
        CompletableFuture<HttpResponse<String>> result;
        result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return result.thenApply(LanguageTranslator::parse);
    }

    private static HttpRequest buildRequest(String text, Language targetLanguage) {
        String authKeyURI = "auth_key=" + DEEPL_TOKEN;
        String textURI = "text=" + text;
        String targetLanguageURI = "target_lang=" + targetLanguage.toISO639_1();
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
            translation =  mapper.readValue(jsonBody, DeeplTranslation.class);
        }catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
            translation = new DeeplTranslation();
            translation.setText("error");
            translation.setDetected_source_language("error");
        }
        return translation;
    }

    public DeeplTranslation[] waitOnTranslationsComplete(ArrayList<CompletableFuture<DeeplTranslation>> futures) throws ExecutionException, InterruptedException {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        DeeplTranslation[] result = new DeeplTranslation[futures.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = futures.get(i).get();
        }
        return result;
    }
}
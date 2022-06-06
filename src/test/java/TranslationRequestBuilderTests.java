import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslationRequestBuilderTests {

    @Test
    void WhenBuildingRequestThenURIShouldBeCorrect() {
        HttpRequest request = getGenericRequest();
        assertEquals(0, request.uri().compareTo(buildGenericURI()));
    }


    private HttpRequest getGenericRequest() {
        return TranslationRequestBuilder.buildRequest("abcd", Language.EN);
    }

    private URI buildGenericURI() {
        return URI.create(
                        "https://api-free.deepl.com/v2/translate?" +
                        "auth_key=" + System.getenv("DEEPL_TOKEN") +
                        "&text=" + "abcd" +
                        "&target_lang=" + Language.EN.toISO639_1()
        );
    }
}

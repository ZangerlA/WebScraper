import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import org.junit.jupiter.api.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTests {

    private static JsonParser parser;

    @BeforeAll
    static void setup() {
        parser = new JsonParser();
    }

    @Test
    void WhenParsingValidJsonThenShouldNotThrow() {
        String json = createJsonString();
        assertDoesNotThrow(() -> parser.parse(json, DeeplResponse.class));
    }

    @Test
    void WhenProvidingInvalidDataClassThenParserShouldThrow() {
        String json = createJsonString();
        assertThrows(DatabindException.class, ()-> parser.parse(json, DeeplTranslation.class));
    }

    @Test
    void WhenProvidingInvalidJsonThenParserShouldThrow() {
        String json = createInvalidJsonString();
        assertThrows(StreamReadException.class, ()-> parser.parse(json, DeeplResponse.class));
    }

    @Test
    void WhenParsingValidJsonThenDataClassShouldBeFilled() throws JsonProcessingException {
        String json = createJsonString();
        DeeplResponse response = parser.parse(json, DeeplResponse.class);
        assertNotEquals(Collections.EMPTY_LIST, response.getTranslations());
    }

    @Test
    void WhenParsingValidJsonThenDataClassTextShouldBeSet() throws JsonProcessingException {
        DeeplTranslation translation = createGenericTranslation();

        assertEquals("Hallo Welt", translation.getText());
    }

    @Test
    void WhenParsingValidJsonThenDataClassDetectedLanguageShouldBeSet() throws JsonProcessingException {
        DeeplTranslation translation = createGenericTranslation();

        assertEquals("EN", translation.getDetected_source_language());
    }

    private DeeplTranslation createGenericTranslation() throws JsonProcessingException {
        String json = createJsonString();
        DeeplResponse response = parser.parse(json, DeeplResponse.class);
        return response.getElementAt(0);
    }

    private String createJsonString() {
        return "{\"translations\":[{\"detected_source_language\":\"EN\",\"text\":\"Hallo Welt\"}]}";
    }

    private String createInvalidJsonString() {
        return "{\"translations\"={\"detected_source_language\"=\"EN\",\"text\"=\"Hallo Welt\"}}";
    }
}


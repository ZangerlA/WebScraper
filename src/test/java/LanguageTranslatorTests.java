import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageTranslatorTests {

    static LanguageTranslator translator;

    @BeforeAll
    static void setup() {
        translator = new LanguageTranslator();
    }

    @Test
    void WhenTranslatingEnglishToGermanThenShouldReturnCorrectTranslation() throws ExecutionException, InterruptedException {
        DeeplTranslation translation = getTranslation("Hello World", Language.DE);

        assertEquals("Hallo Welt", translation.getText());
    }

    @Test
    void WhenTranslatingGermanToEnglishThenSourceLanguageShouldBeGerman() throws ExecutionException, InterruptedException {
        DeeplTranslation translation = getTranslation("Hallo Welt", Language.EN);

        assertEquals(Language.DE.toString(), translation.getDetected_source_language());
    }

    @Test
    void WhenTranslatingWithInvalidTargetLanguageThenShouldThrow() {
        assertThrows(RuntimeException.class, ()-> getTranslation("test", Language.NONE));
    }

    private DeeplTranslation getTranslation(String text, Language language) throws ExecutionException, InterruptedException {
        CompletableFuture<DeeplTranslation> future;
        future = translator.translate(text, language);
        return future.get();
    }
}

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
        DeeplTranslation translation = testTranslate("Hello World", Language.DE);
        System.out.println(translation.getText());
        assertEquals("Hallo Welt", translation.getText());
    }

    @Test
    void WhenTranslatingGermanToEnglishThenSourceLanguageShouldBeGerman() throws ExecutionException, InterruptedException {
        DeeplTranslation translation = testTranslate("Übersetzung", Language.EN);

        assertEquals(Language.DE.toString(), translation.getDetected_source_language());
    }

    @Test
    void WhenTranslatingWithInvalidTargetLanguageThenShouldThrow() {
        assertThrows(RuntimeException.class, ()-> testTranslate("test", Language.NONE));
    }

    @Test
    void WhenTranslatingThenResponseShouldNotBeNull() throws ExecutionException, InterruptedException {
        assertNotNull(testTranslate("Hello World", Language.DE));
    }

    private DeeplTranslation testTranslate(String text, Language language) throws ExecutionException, InterruptedException {
        CompletableFuture<DeeplTranslation> future;
        future = translator.translate(text, language);
        return future.get();
    }
}

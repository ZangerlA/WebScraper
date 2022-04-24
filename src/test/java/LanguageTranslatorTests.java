import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageTranslatorTests {

    static LanguageTranslator translator;
    static final String standardTestText = "Hello World";
    static final Language standardTestTargetLanguage = Language.EN;

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
        assertThrows(RuntimeException.class, ()-> testTranslate(standardTestText, Language.NONE));
    }

    @Test
    void WhenTranslatingThenResponseShouldNotBeNull() throws ExecutionException, InterruptedException {
        assertNotNull(testTranslate(standardTestText, standardTestTargetLanguage));
    }

    @Test
    void WhenTranslatingThenPromiseShouldResolve() {
        CompletableFuture<DeeplTranslation> future = translator.translate(standardTestText, standardTestTargetLanguage);
        assertDoesNotThrow(()-> future.get());
    }

    @Test
    void WhenTryingToTranslateSpecialCharactersThenShouldNotThrow() {
        assertDoesNotThrow(()-> testTranslate("'\"\\\t\b\r\f\n", standardTestTargetLanguage));
    }

    private DeeplTranslation testTranslate(String text, Language language) throws ExecutionException, InterruptedException {
        CompletableFuture<DeeplTranslation> future = translator.translate(text, language);
        return future.get();
    }
}

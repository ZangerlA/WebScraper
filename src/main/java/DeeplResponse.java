import java.util.ArrayList;

public class DeeplResponse {
    private ArrayList<DeeplTranslation> translations;


    public ArrayList<DeeplTranslation> getTranslations() {
        return translations;
    }

    public DeeplTranslation getElementAt(int index) {
        return translations.get(index);
    }
}




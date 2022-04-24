import java.util.Locale;

public enum Language {
    DE,
    EN,
    FR,
    IT,
    EL,
    ES,
    CS,
    BG,
    DA,
    RU,
    JA,
    ZH,
    NONE;

    public String toISO639_1() {
        if (this == EN) {
            return toString().toLowerCase().concat("-US");
        }
        return this == NONE ? throwException() : toString().toLowerCase();
    }

    private static String throwException() {
        throw new RuntimeException("Not an ISO Standard.");
    }
}

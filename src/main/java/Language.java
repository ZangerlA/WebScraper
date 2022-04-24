import java.util.Locale;

public enum Language {
    DE,
    EN,
    ES,
    FR,
    IT,
    JA,
    NL,
    PL,
    RU,
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

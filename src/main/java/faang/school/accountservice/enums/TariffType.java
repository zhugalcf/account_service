package faang.school.accountservice.enums;

public enum TariffType {
    BASIC("basic"),
    PROMO("promo"),
    SUBSCRIPTION("subscription"),
    FAMILY("family"),
    STUDENT("student"),
    BUSINESS("business"),
    UNLIMITED("unlimited"),
    PREMIUM("premium"),
    CUSTOM("custom");

    private final String type;

    TariffType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}

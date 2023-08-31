package faang.school.accountservice.enums;

public enum AccountType {
    SETTLEMENT("settlement"),
    CURRENCY("currency"),
    CREDIT("credit"),
    DEPOSIT("deposit"),
    ;
    private final String type;

    AccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

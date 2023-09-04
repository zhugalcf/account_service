package faang.school.accountservice.enums;

public enum AccountType {
    DEBIT("debit"),
    CURRENCY("currency"),
    CREDIT("credit"),
    DEPOSIT("deposit"),
    SAVINGS("savings"),
    ;
    private final String type;

    AccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

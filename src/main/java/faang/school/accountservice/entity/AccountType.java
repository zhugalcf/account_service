package faang.school.accountservice.entity;

public enum AccountType {
    SAVINGS("5236000000000000"),
    DEBIT("4200000000000000");
    private final String accountNumber;

    AccountType(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAccountNumber() {
        return Long.parseLong(accountNumber);
    }
}
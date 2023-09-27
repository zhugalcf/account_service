package faang.school.accountservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    FOREIGN_CURRENCY_ACCOUNT("5230"),
    DEPOSIT_ACCOUNT("5300"),
    CREDIT_ACCOUNT("5420"),
    PAYMENT_ACCOUNT("5510");

    private final String associatedString;
}

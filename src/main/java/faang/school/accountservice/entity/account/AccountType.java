package faang.school.accountservice.entity.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    CHECKING_ACCOUNT("5233"),
    SAVINGS_ACCOUNT("4255"),
    INVESTMENT_ACCOUNT("2352"),
    FOREIGN_CURRENCY_ACCOUNT("4321"),
    LEGAL_ENTITY_CURRENT_ACCOUNT("4212");

    private final String getNumberAccountType;
}

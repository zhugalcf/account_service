package faang.school.accountservice.entity.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    INDIVIDUAL_CHECKING("8888"),
    BUSINESS_CHECKING("9999"),
    FOREIGN_CURRENCY("7777");

    private final String value;
}

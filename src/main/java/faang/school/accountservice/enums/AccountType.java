package faang.school.accountservice.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    INDIVIDUAL("5400"),
    ENTITY("5500"),
    FOREIGN_CURRENCY("5600");

    private final String identityString;
    }

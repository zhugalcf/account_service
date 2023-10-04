package faang.school.accountservice.model.account.number;

import faang.school.accountservice.enums.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;

public class AccountNumberId implements Serializable {

    private String account_number;

    @Enumerated(EnumType.STRING)
    private AccountType type;
}

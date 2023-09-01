package faang.school.accountservice.dto;

import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Currency;
import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private String accountNumber;
    private String typeOfOwner;
    private AccountType accountType;
    private Currency currency;
    private AccountStatus status;
    private Long version;
}

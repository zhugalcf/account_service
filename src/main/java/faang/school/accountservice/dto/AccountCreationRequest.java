package faang.school.accountservice.dto;

import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AccountCreationRequest {
    private Long ownerId;
    private AccountType type;
    private Currency currency;
    private AccountStatus status;
}

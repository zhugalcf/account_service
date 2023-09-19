package faang.school.accountservice.dto;

import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.enums.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class AccountRequestDto {
    private OwnerType ownerType;
    private long ownerId;
    private AccountType accountType;
    private Currency currency;
    private AccountStatus status;
    private LocalDateTime createdAt;
}

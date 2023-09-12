package faang.school.accountservice.dto;

import faang.school.accountservice.model.AccountStatus;
import faang.school.accountservice.model.AccountType;
import faang.school.accountservice.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private Long id;
    private String number;
    private Long userId;
    private Long projectId;
    private AccountType type;
    private Currency currency;
    private AccountStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant closedAt;
    private long version;
}

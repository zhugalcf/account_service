package faang.school.accountservice.dto.account;

import faang.school.accountservice.dto.OwnerDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Currency;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAccountDto {
    private Long id;
    private String number;
    private OwnerDto owner;
    private AccountType type;
    private Currency currency;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
}

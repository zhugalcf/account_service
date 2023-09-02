package faang.school.accountservice.dto;

import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long id;
    private String accountNumber;
    @NotBlank(message = "Owner type is empty")
    private String typeOfOwner;
    private Long ownerId;
    @NotBlank(message = "Should be a valid account type")
    private AccountType accountType;
    private Currency currency;
    private AccountStatus status;
    private Long version;
    private LocalDateTime createdAt;
}

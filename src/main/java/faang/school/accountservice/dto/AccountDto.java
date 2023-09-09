package faang.school.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Account number is required")
    @Pattern(message = "Account number must be between 12 and 20 characters", regexp = "^(.{12,20})$")
    private String accountNumber;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Account currency is required")
    @Pattern(message = "Currency code must be 3 three characters in upper case", regexp = "^[A-Z]{3}$")
    private String currencyCode;

    private AccountStatus accountStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

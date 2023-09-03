package faang.school.accountservice.dto.account;

import faang.school.accountservice.annotations.ValidOwner;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.enums.OwnerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidOwner
public class CreateAccountDto {
    @NotBlank
    @Size(min = 12, max = 20)
    private String number;
    @Positive
    @NotNull
    private Long ownerId;
    @NotNull
    private OwnerType ownerType;
    @NotNull
    private AccountType type;
    @NotNull
    private Currency currency;
}

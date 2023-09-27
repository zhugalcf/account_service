package faang.school.accountservice.dto.account;

import faang.school.accountservice.annotations.ValidOwner;
import faang.school.accountservice.dto.OwnerDto;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.Balance;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidOwner
public class CreateAccountDto {
    @NotNull
    @Size(min = 12, max = 20)
    private String number;
    @NotNull
    private OwnerDto owner;
    @NotNull
    private AccountType type;
    @NotNull
    private Currency currency;
}

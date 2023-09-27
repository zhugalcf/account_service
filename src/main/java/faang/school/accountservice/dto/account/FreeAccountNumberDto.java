package faang.school.accountservice.dto.account;

import faang.school.accountservice.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeAccountNumberDto {
    @NotNull
    private Long id;
    @NotNull
    private AccountType accountType;
    @NotNull
    @Size(min = 12, max = 36)
    private String accountNumber;
}

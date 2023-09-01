package faang.school.accountservice.dto;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;

    @NotBlank(message = "Number payment must not be blank")
    @Pattern(regexp = "^.{12,20}$", message = "Number payment must be between 12 and 20 characters")
    private String numberPayment;

    @NotNull(message = "Owner id must not be null")
    private Long ownerId;

    @NotNull(message = "Type must not be null")
    private AccountType type;

    @NotNull(message = "Currency must not be null")
    private Currency currency;

    @NotNull(message = "Status must not be null")
    private Status status;
}

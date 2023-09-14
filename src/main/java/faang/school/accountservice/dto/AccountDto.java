package faang.school.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Status;
import jakarta.validation.constraints.NotNull;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String accountNumber;

    @NotNull(message = "Owner id must not be null")
    private Long ownerId;

    @NotNull(message = "Type must not be null")
    private AccountType type;

    @NotNull(message = "Currency id must not be null")
    private Long currencyId;

    @NotNull(message = "Status must not be null")
    private Status status;
}

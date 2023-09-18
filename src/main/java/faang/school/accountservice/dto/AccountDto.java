package faang.school.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String number;
    private Long userId;
    private Long projectId;
    private AccountType type;
    private Currency currency;
    private AccountStatus status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant closedAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long version;
}

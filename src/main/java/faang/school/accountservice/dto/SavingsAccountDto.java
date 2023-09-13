package faang.school.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingsAccountDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull(message = "accountId cannot be null")
    private Long accountId;
    private BigDecimal balance;
    @NotNull(message = "current tariff cannot be null")
    private Long current_tariff;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TariffDto tariffDto;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String tariffHistory;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastInterestDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long version;
}
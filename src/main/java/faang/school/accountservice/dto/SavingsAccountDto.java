package faang.school.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccountDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long id;

    @NotNull(message = "Account number must not be null")
    public Long accountId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String historyTariff;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime latestReportDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime updatedAt;

    @NotNull(message = "The savings account must have a tariff")
    public Long tariffId;
}

package faang.school.accountservice.dto;

import faang.school.accountservice.enums.TariffType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingsAccountCreateDto {
    @NotNull(message = "Account id cant be null")
    @Min(value = 1, message = "Account id should not be less then 1")
    private long accountId;
    @NotNull(message = "Tariff type cant be null")
    private TariffType tariffType;
}

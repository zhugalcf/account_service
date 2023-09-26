package faang.school.accountservice.dto;

import faang.school.accountservice.enums.TariffType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTariffDto {
    @NotNull(message = "Tariff type cant be null")
    private TariffType type;
    @NotNull(message = "Rate percent cant be null")
    private float ratePercent;
}

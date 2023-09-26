package faang.school.accountservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTariffDto {
    @NotNull(message = "Tariff id cant be null")
    private long tariffId;
    @NotNull(message = "Tariff rate percent cant be null")
    private float ratePercent;
}

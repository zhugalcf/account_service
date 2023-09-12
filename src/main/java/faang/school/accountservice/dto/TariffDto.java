package faang.school.accountservice.dto;

import faang.school.accountservice.model.saving.TariffType;
import jakarta.validation.constraints.Max;
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
public class TariffDto {
    private Long id;
    @NotNull(message = "tariffType cannot be null")
    private TariffType tariffType;
    @Min(value = 0, message = "currentRate must be greater than 0")
    @Max(value = 100, message = "currentRate must be less than 100")
    private float currentRate;
}
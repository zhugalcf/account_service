package faang.school.accountservice.dto;

import faang.school.accountservice.entity.TariffType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffDto {
    private Long id;
    @NotNull(message = "Tariff type is required")
    private TariffType tariffType;
    private List<Double> rates;
}
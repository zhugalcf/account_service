package faang.school.accountservice.dto;

import faang.school.accountservice.entity.TypeTariff;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffDto {

    private Long id;

    @NotNull(message = "Type must not be null")
    private TypeTariff type;

    @NotNull(message = "Rate must not be null")
    private Double rate;
}

package faang.school.accountservice.dto.tariff;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TariffCreateDto {
    @Size(max = 64, message = "max size 64")
    private String typeTariff;
    @DecimalMin(value = "0", message = "min size 0")
    @DecimalMax(value = "20", message = "max size 20")
    private BigDecimal bet;
}

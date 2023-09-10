package faang.school.accountservice.dto;

import faang.school.accountservice.enums.TariffType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TariffDto {
    private long id;
    private TariffType type;
    private BigDecimal ratePercent;
}

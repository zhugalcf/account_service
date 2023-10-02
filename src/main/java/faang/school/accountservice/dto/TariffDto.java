package faang.school.accountservice.dto;

import faang.school.accountservice.entity.TariffType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffDto {
    private Long id;
    private TariffType tariffType;
    private List<BigDecimal> rates;
}
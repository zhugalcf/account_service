package faang.school.accountservice.dto;

import faang.school.accountservice.dto.tariff.TariffDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OpenSavingsScoreDto {
    private String number;
    private String tariffHistory;
    private BigDecimal amount;
    private TariffDto tariffDto;
}

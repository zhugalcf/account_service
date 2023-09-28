package faang.school.accountservice.dto.tariff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {
    private String typeTariff;
    private float bet;
    private String bettingHistory;
}

package faang.school.accountservice.validator;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.exception.TariffValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class TariffValidator {
    public void validateTariff(TariffDto tariff) throws TariffValidationException {
        if (tariff == null) {
            throw new TariffValidationException("Tariff cannot be null");
        }

        if (tariff.getTariffType() == null) {
            throw new TariffValidationException("Tariff type is required");
        }

        if (CollectionUtils.isEmpty(tariff.getRates())) {
            throw new TariffValidationException("Tariff rates are required");
        }
    }
}
package faang.school.accountservice.service;

import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

    private final RateRepository rateRepository;

    @Transactional
    public Rate assignRatePercentToTariff(float percent, Tariff tariff) {
        Rate rate = Rate.builder()
                .percent(percent)
                .tariff(tariff)
                .build();

        return rateRepository.save(rate);
    }
}

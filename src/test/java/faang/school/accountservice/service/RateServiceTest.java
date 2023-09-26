package faang.school.accountservice.service;

import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.repository.RateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateServiceTest {

    @Mock
    private RateRepository rateRepository;
    @InjectMocks
    private RateService rateService;

    @Test
    void assignRatePercentToTariffTest() {
        LocalDateTime createdAt = LocalDateTime.now().minusMonths(1);
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);

        Tariff tariff = Tariff.builder()
                .id(5)
                .type(TariffType.BASIC)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        Rate saveRate = Rate.builder()
                .percent(2.2f)
                .tariff(tariff)
                .build();
        Rate expected = Rate.builder()
                .id(1)
                .percent(2.2f)
                .tariff(tariff)
                .build();

        when(rateRepository.save(saveRate)).thenReturn(expected);

        Rate result = rateService.assignRatePercentToTariff(2.2f, tariff);

        assertEquals(expected, result);
        verify(rateRepository).save(saveRate);
    }
}
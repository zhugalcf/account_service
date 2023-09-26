package faang.school.accountservice.service;

import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.repository.TariffHistoryRepository;
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
class TariffHistoryServiceTest {

    @Mock
    private TariffHistoryRepository historyRepository;
    @InjectMocks
    private TariffHistoryService tariffHistoryService;

    @Test
    void saveTariffHistoryTest() {
        LocalDateTime lastModifiedDate = LocalDateTime.now().minusDays(10);
        SavingsAccount savingsAccount = SavingsAccount.builder()
                .id(5)
                .build();
        Tariff tariff = Tariff.builder()
                .id(10)
                .build();
        TariffHistory tariffHistory = TariffHistory.builder()
                .savingsAccount(savingsAccount)
                .tariff(tariff)
                .lastModifiedDate(lastModifiedDate)
                .build();
        TariffHistory expected = TariffHistory.builder()
                .id(1)
                .savingsAccount(savingsAccount)
                .tariff(tariff)
                .lastModifiedDate(lastModifiedDate)
                .build();

        when(historyRepository.save(tariffHistory)).thenReturn(expected);

        TariffHistory result = tariffHistoryService.saveTariffHistory(tariffHistory);

        assertEquals(expected, result);

        verify(historyRepository).save(tariffHistory);
    }

}
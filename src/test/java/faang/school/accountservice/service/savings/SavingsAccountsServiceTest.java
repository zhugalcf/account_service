package faang.school.accountservice.service.savings;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.config.context.UserContext;
import faang.school.accountservice.dto.tariff.TariffDto;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.TariffRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavingsAccountsServiceTest {
    @Mock
    private UserContext userContext;
    @Mock
    private TariffRepository tariffRepository;
    @Mock
    private SavingsAccountRepository savingsAccountRepository;
    @InjectMocks
    private SavingsAccountsService savingsAccountsService;

    @Test
    void getTariffScore() {
        SavingsAccountsService service = new SavingsAccountsService(new ObjectMapper(), userContext, tariffRepository, savingsAccountRepository);
        Tariff tariff = new Tariff();
        tariff.setId(1);
        tariff.setBet(2.5f);
        tariff.setTypeTariff("Base type");
        tariff.setBettingHistory("[1]");
        when(userContext.getUserId()).thenReturn(1L);
        when(savingsAccountRepository.tariffHistory(1, 1)).thenReturn(Optional.of("[1]"));
        when(tariffRepository.getReferenceById(1L)).thenReturn(tariff);
        TariffDto result = service.getTariffScore(1);
        TariffDto expected = new TariffDto("Base type", 2.5f, "[1]");
        assertEquals(expected.getTypeTariff(), result.getTypeTariff());
    }

    @Test
    void openScore() {
        SavingsAccount dto = SavingsAccount.builder().number("00000000000000").tariffHistory("[0]").amount(BigDecimal.valueOf(0))
                .account(Account.builder().id(1L).build()).build();
        when(userContext.getUserId()).thenReturn(1L);
        when(tariffRepository.findByType("Basic")).thenReturn(new Tariff());
        when(savingsAccountRepository.save(dto)).thenReturn(dto);
        savingsAccountsService.openScore();
        verify(savingsAccountRepository, times(1)).save(dto);

    }
}
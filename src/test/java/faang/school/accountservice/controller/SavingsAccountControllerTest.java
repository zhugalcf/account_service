package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.service.SavingsAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SavingsAccountControllerTest {
    @InjectMocks
    private SavingsAccountController savingsAccountController;
    @Mock
    private SavingsAccountService savingsAccountService;

    private SavingsAccountDto savingsAccountDto;
    private TariffDto tariffDto;
    private AccountDto accountDto;

    @BeforeEach
    public void setUp() {
        accountDto = AccountDto.builder()
                .id(1L)
                .build();

        savingsAccountDto = SavingsAccountDto.builder()
                .id(1L)
                .accountId(accountDto.getId())
                .build();

        tariffDto = TariffDto.builder()
                .id(1L)
                .rate(new BigDecimal("0.5"))
                .build();
    }


    @Test
    public void testGetSavingsAccount() {
        savingsAccountController.get(1L);
        verify(savingsAccountService).get(1L);
    }

    @Test
    public void testOpenSavingsAccount() {
        when(savingsAccountService.openSavingsAccount(1L, "USD")).thenReturn(savingsAccountDto);

        savingsAccountController.openSavingsAccount(1L, "USD");

        verify(savingsAccountService).openSavingsAccount(1L, "USD");
    }

    @Test
    public void testGetCurrentTariffAndRateByClientId() {
        when(savingsAccountService.getCurrentTariffAndRateByClientId(1L)).thenReturn(tariffDto);

        TariffDto result = savingsAccountController.getCurrentTariffAndRateByClientId(1L);

        verify(savingsAccountService).getCurrentTariffAndRateByClientId(1L);
        assertNotNull(result);
        assertEquals(tariffDto, result);
    }
}
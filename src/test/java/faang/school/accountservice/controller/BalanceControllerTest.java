package faang.school.accountservice.controller;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.service.BalanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;
    @InjectMocks
    private BalanceController balanceController;
    private final long ACCOUNT_ID = 1L;

    @Test
    void testGetBalance() {
        balanceController.getBalance(ACCOUNT_ID);
        verify(balanceService).getBalance(ACCOUNT_ID);
    }

    @Test
    void testCreateBalance() {
        balanceController.createBalance(ACCOUNT_ID);
        verify(balanceService).create(ACCOUNT_ID);
    }

    @Test
    void testUpdateBalance() {
        BalanceDto balanceDto = BalanceDto.builder()
                .id(1L)
                .accountNumber("WANRLTW123456789")
                .version(2L)
                .build();
        balanceController.updateBalance(balanceDto);
        verify(balanceService).update(balanceDto);
    }
}

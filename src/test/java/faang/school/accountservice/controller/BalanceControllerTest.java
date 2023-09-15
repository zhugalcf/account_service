package faang.school.accountservice.controller;

import faang.school.accountservice.service.BalanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;
    @InjectMocks
    private BalanceController balanceController;
    private final String ACCOUNT_NUMBER = "WANRLTW123456789";

    @Test
    void testGetBalance() {
        balanceController.getBalance(ACCOUNT_NUMBER);
        Mockito.verify(balanceService).getBalance(ACCOUNT_NUMBER);
    }
}

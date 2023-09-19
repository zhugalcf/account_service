package faang.school.accountservice.controller;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private BalanceController balanceController;

    private BalanceDto balanceDto;

    @BeforeEach
    public void setUp() {
        balanceDto = BalanceDto.builder().build();
    }

    @Test
    public void testGetBalance() {
        balanceController.getBalance(1L);
        verify(balanceService).getBalanceByAccountId(1L);
    }

    @Test
    public void testCreateBalance() {
        balanceController.createBalance(1L, balanceDto);
        verify(balanceService).createBalance(1L, balanceDto);
    }

    @Test
    public void testUpdateBalance() {
        balanceController.updateBalance(1L, balanceDto);
        verify(balanceService).updateBalance(1L, balanceDto);
    }

    @Test
    public void testDeposit() {
        balanceController.deposit(1L, BigDecimal.TEN);
        verify(balanceService).deposit(1L, BigDecimal.TEN);
    }

    @Test
    public void testWithdraw() {
        balanceController.withdraw(1L, BigDecimal.TEN);
        verify(balanceService).withdraw(1L, BigDecimal.TEN);
    }

    @Test
    public void testTransfer() {
        balanceController.transfer(1L, 2L, BigDecimal.TEN);
        verify(balanceService).transfer(1L, 2L, BigDecimal.TEN);
    }
}
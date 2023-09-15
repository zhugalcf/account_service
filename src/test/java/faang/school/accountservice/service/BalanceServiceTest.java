package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.exception.IncorrectAccountBalanceLengthException;
import faang.school.accountservice.mapper.BalanceMapperImpl;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;
    @Spy
    private BalanceMapperImpl balanceMapper;
    @InjectMocks
    private BalanceService balanceService;

    private final String INCORRECT_ACCOUNT_NUMBER = "HESOYMY";
    private final String ACCOUNT_NUMBER = "WANRLTW123456789";
    private Balance balance;
    private BalanceDto balanceDto;

    @BeforeEach
    void initData() {
        balance = Balance.builder()
                .accountNumber(new Account(1L, ACCOUNT_NUMBER, balance))
                .currentAuthorizationBalance(100.0)
                .currentActualBalance(100.0)
                .build();
        balanceDto = BalanceDto.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .currentAuthorizationBalance(100.0)
                .currentActualBalance(100.0)
                .version(0L)
                .build();
    }

    @Test
    void testGetBalanceWithEmptyNumber() {
        assertThrows(IllegalArgumentException.class, () -> balanceService.getBalance(""));
    }

    @Test
    void testGetBalanceWithIncorrectNumber() {
        assertThrows(IncorrectAccountBalanceLengthException.class, () -> balanceService.getBalance(INCORRECT_ACCOUNT_NUMBER));
    }

    @Test
    void testGetBalance() {
        when(balanceRepository.findBalanceByAccountNumber(ACCOUNT_NUMBER)).thenReturn(balance);
        BalanceDto actualBalanceDto = balanceService.getBalance(ACCOUNT_NUMBER);
        assertEquals(balanceDto, actualBalanceDto);
    }

    @Test
    void testCreate() {
        balanceService.create(balanceDto);
        balance.setCreatedAt(LocalDateTime.now());
        balance.setUpdatedAt(LocalDateTime.now());
        balance.setVersion(1L);

        verify(balanceRepository).save(any());
    }

    @Test
    void testUpdate() {
        when(balanceRepository.findBalanceByAccountNumber(ACCOUNT_NUMBER)).thenReturn(balance);
        balanceService.update(balanceDto);
        verify(balanceRepository).findBalanceByAccountNumber(ACCOUNT_NUMBER);
    }
}

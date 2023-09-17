package faang.school.accountservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.balance.Balance;
import faang.school.accountservice.repository.BalanceRepository;
import faang.school.accountservice.excpetion.EntityNotFoundException;
import faang.school.accountservice.mapper.BalanceMapper;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BalanceServiceTest {

    @InjectMocks
    private BalanceService balanceService;
    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private BalanceMapper balanceMapper;
    @Mock
    private AccountService accountService;

    private BalanceDto balanceDto;
    private Balance balance;
    private Account account;


    @BeforeEach
    public void setUp() {
        balanceDto = BalanceDto.builder().build();
        balance = Balance.builder().build();
        account = Account.builder().build();
    }

    @Test
    public void testGetBalanceByAccountId_Success() {
        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(balance));
        when(balanceMapper.toDto(balance)).thenReturn(balanceDto);

        BalanceDto result = balanceService.getBalanceByAccountId(1L);
        assertNotNull(result);
        assertEquals(balanceDto, result);
    }

    @Test
    public void testCreateBalance_Success() {
        when(accountService.getAccount(1L)).thenReturn(account);
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
        when(balanceMapper.toDto(balance)).thenReturn(balanceDto);

        BalanceDto result = balanceService.createBalance(1L, balanceDto);
        assertNotNull(result);
        assertEquals(balanceDto, result);
    }

    @Test
    public void testUpdateBalance_Success() {
        when(balanceRepository.findByAccountId(1L)).thenReturn(java.util.Optional.of(balance));
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
        when(balanceMapper.toDto(balance)).thenReturn(balanceDto);

        BalanceDto result = balanceService.updateBalance(1L, balanceDto);
        assertNotNull(result);
        assertEquals(balanceDto, result);
    }

    @Test
    public void testGetBalanceByAccountId_Fail() {
        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> balanceService.getBalanceByAccountId(1L));
    }
}

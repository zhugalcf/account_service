package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.balance.Balance;
import faang.school.accountservice.exception.DuplicateBalanceException;
import faang.school.accountservice.exception.EntityNotFoundException;
import faang.school.accountservice.exception.InsufficientBalanceException;
import faang.school.accountservice.mapper.BalanceMapperImpl;
import faang.school.accountservice.repository.BalanceRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BalanceServiceTest {

    @InjectMocks
    private BalanceService balanceService;
    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private BalanceMapperImpl balanceMapper;
    @Mock
    private AccountService accountService;

    private BalanceDto balanceDto;
    private Balance balance;
    private Account account;


    @BeforeEach
    public void setUp() {
        account = Account.builder().build();
        balanceDto = BalanceDto.builder().build();
        balance = Balance.builder().account(account)
                .authorizationBalance(BigDecimal.ZERO)
                .actualBalance(BigDecimal.ZERO)
                .build();
    }

    @Test
    public void testGetBalanceByAccountIdSuccess() {
        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(balance));
        when(balanceMapper.toDto(balance)).thenReturn(balanceDto);

        BalanceDto result = balanceService.getBalanceByAccountId(1L);
        assertNotNull(result);
        assertEquals(balanceDto, result);
    }

    @Test
    public void testCreateBalanceSuccess() {
        when(accountService.getAccount(1L)).thenReturn(account);
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
        when(balanceMapper.toDto(balance)).thenReturn(balanceDto);

        BalanceDto result = balanceService.createBalance(1L, balanceDto);
        assertNotNull(result);
        assertEquals(balanceDto, result);
    }

    @Test
    public void testCreateBalanceIfDuplicateExists_ExceptionThrown() {
        when(accountService.getAccount(1L)).thenReturn(account);
        when(balanceRepository.existsByAccountId(1L)).thenReturn(true);

        assertThrows(DuplicateBalanceException.class, () -> balanceService.createBalance(1L, balanceDto));
    }

    @Test
    public void testUpdateBalanceSuccess() {
        when(balanceRepository.findByAccountId(1L)).thenReturn(java.util.Optional.of(balance));
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
        when(balanceMapper.toDto(balance)).thenReturn(balanceDto);

        BalanceDto result = balanceService.updateBalance(1L, balanceDto);
        assertNotNull(result);
        assertEquals(balanceDto, result);
    }

    @Test
    public void testGetBalanceByAccountId_ExceptionThrown() {
        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> balanceService.getBalanceByAccountId(1L));
    }

    @Test
    public void testDepositSuccess() {
        balance.setAuthorizationBalance(BigDecimal.ZERO);
        balance.setActualBalance(BigDecimal.ZERO);

        Balance savedBalance = new Balance();
        savedBalance.setAuthorizationBalance(BigDecimal.TEN);
        savedBalance.setActualBalance(BigDecimal.TEN);

        BalanceDto expectedDto = BalanceDto.builder()
                .authorizationBalance(savedBalance.getAuthorizationBalance())
                .actualBalance(savedBalance.getActualBalance())
                .build();

        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(balance));
        when(balanceRepository.save(balance)).thenReturn(savedBalance);
        when(balanceMapper.toDto(savedBalance)).thenReturn(expectedDto);

        BalanceDto result = balanceService.deposit(1L, BigDecimal.TEN);

        assertEquals(BigDecimal.TEN, result.getAuthorizationBalance());
        assertEquals(BigDecimal.TEN, result.getActualBalance());

        verify(balanceRepository, times(1)).findByAccountId(1L);
        verify(balanceRepository, times(1)).save(balance);
        verify(balanceMapper, times(1)).toDto(savedBalance);
    }

    @Test
    public void testDepositWithInvalidAccountId_ExceptionThrown() {
        Long invalidAccountId = -1L;
        BigDecimal amount = BigDecimal.TEN;

        assertThrows(EntityNotFoundException.class, () -> balanceService.deposit(invalidAccountId, amount));

        Mockito.verifyNoInteractions(balanceMapper);
    }

    @Test
    public void testDepositWithNegativeAmount_ExceptionThrown() {
        Long accountId = 1L;
        BigDecimal negativeAmount = BigDecimal.valueOf(-10);

        assertThrows(EntityNotFoundException.class, () -> balanceService.deposit(accountId, negativeAmount));

        Mockito.verifyNoInteractions(balanceMapper);
    }

    @Test
    public void testDepositWithBalanceNotFound_ExceptionThrown() {
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.TEN;

        when(balanceRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> balanceService.deposit(accountId, amount));

        verify(balanceRepository, times(1)).findByAccountId(accountId);
        Mockito.verifyNoInteractions(balanceMapper);
    }

    @Test
    void testWithdrawWithSufficientAuthorizationBalanceSuccess() {
        Long accountId = 123L;
        BigDecimal amount = new BigDecimal("100.00");

        balance.setAuthorizationBalance(new BigDecimal("200.00"));
        balance.setActualBalance(new BigDecimal("500.00"));

        when(balanceRepository.findByAccountId(accountId)).thenReturn(Optional.ofNullable(balance));

        Balance updatedBalance = new Balance();
        updatedBalance.setAuthorizationBalance(new BigDecimal("100.00"));
        updatedBalance.setActualBalance(new BigDecimal("400.00"));

        when(balanceRepository.save(balance)).thenReturn(updatedBalance);

        BalanceDto expectedDto = new BalanceDto();
        expectedDto.setAuthorizationBalance(new BigDecimal("100.00"));
        expectedDto.setActualBalance(new BigDecimal("400.00"));

        when(balanceMapper.toDto(updatedBalance)).thenReturn(expectedDto);

        BalanceDto result = balanceService.withdraw(accountId, amount);

        assertEquals(expectedDto, result);
        assertEquals(new BigDecimal("100.00"), balance.getAuthorizationBalance());
        assertEquals(new BigDecimal("400.00"), balance.getActualBalance());
    }

    @Test
    void testWithdrawWithInsufficientAuthorizationBalance_ExceptionThrown() {
        Long accountId = 123L;
        BigDecimal amount = new BigDecimal("100.00");

        balance.setAuthorizationBalance(new BigDecimal("50.00"));
        balance.setActualBalance(new BigDecimal("500.00"));

        when(balanceRepository.findByAccountId(accountId)).thenReturn(Optional.ofNullable(balance));

        assertThrows(InsufficientBalanceException.class, () -> balanceService.withdraw(accountId, amount));
    }

    @Test
    public void testTransferWithSufficientAuthorizationBalanceSuccess() {
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100);

        Balance senderBalance = getSenderBalance(senderId);

        Balance receiverBalance = getReceiverBalance(receiverId);

        List<Balance> balances = Arrays.asList(senderBalance, receiverBalance);

        when(balanceRepository.findAllByAccountIds(Arrays.asList(senderId, receiverId))).thenReturn(balances);

        balanceService.transfer(senderId, receiverId, amount);

        verify(balanceRepository, times(1)).saveAll(balances);
        assertEquals(BigDecimal.valueOf(100), senderBalance.getAuthorizationBalance());
        assertEquals(BigDecimal.valueOf(200), senderBalance.getActualBalance());
        assertEquals(BigDecimal.valueOf(600), receiverBalance.getActualBalance());
    }

    @Test()
    public void testTransferWithNotFoundAccounts_ExceptionThrown() {
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100);

        when(balanceRepository.findAllByAccountIds(Arrays.asList(senderId, receiverId))).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> balanceService.transfer(senderId, receiverId, amount));
    }

    @Test()
    public void testTransferWithInsufficientAuthorizationBalance_ExceptionThrown() {
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal amount = BigDecimal.valueOf(300);

        Balance senderBalance = getSenderBalance(senderId);

        Balance receiverBalance = getReceiverBalance(receiverId);

        List<Balance> balances = Arrays.asList(senderBalance, receiverBalance);

        when(balanceRepository.findAllByAccountIds(Arrays.asList(senderId, receiverId))).thenReturn(balances);

        assertThrows(InsufficientBalanceException.class, () -> balanceService.transfer(senderId, receiverId, amount));
    }

    @NotNull
    private Balance getSenderBalance(Long senderId) {
        Balance senderBalance = new Balance();
        senderBalance.setAccount(Account.builder().id(senderId).build());
        senderBalance.setAuthorizationBalance(BigDecimal.valueOf(200));
        senderBalance.setActualBalance(BigDecimal.valueOf(300));
        return senderBalance;
    }

    @NotNull
    private Balance getReceiverBalance(Long receiverId) {
        Balance receiverBalance = new Balance();
        receiverBalance.setAccount(Account.builder().id(receiverId).build());
        receiverBalance.setActualBalance(BigDecimal.valueOf(500));
        return receiverBalance;
    }
}

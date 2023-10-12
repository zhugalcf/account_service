package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.mapper.BalanceAuditMapperImpl;
import faang.school.accountservice.mapper.BalanceMapperImpl;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.BalanceAudit;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceAuditRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private BalanceAuditRepository balanceAuditRepository;
    @Mock
    private AccountRepository accountRepository;
    @Spy
    private BalanceMapperImpl balanceMapper;
    @Spy
    private BalanceAuditMapperImpl balanceAuditMapper;
    @InjectMocks
    private BalanceService balanceService;

    private final String ACCOUNT_NUMBER = "WANRLTW123456789";
    private final long INCORRECT_ACCOUNT_ID = 0L;
    private final long ACCOUNT_ID = 1L;
    private Balance balance;
    private BalanceDto balanceDto;
    private Account account;

    @BeforeEach
    void initData() {
        account = Account.builder()
                .id(1L)
                .number(ACCOUNT_NUMBER)
                .build();
        balance = Balance.builder()
                .id(1L)
                .account(account)
                .currentAuthorizationBalance(new BigDecimal(100.0))
                .currentActualBalance(new BigDecimal(100.0))
                .version(1L)
                .build();
        balanceDto = BalanceDto.builder()
                .id(1L)
                .accountNumber(ACCOUNT_NUMBER)
                .currentAuthorizationBalance(new BigDecimal(100.0))
                .currentActualBalance(new BigDecimal(100.0))
                .version(1L)
                .build();
    }

    @Test
    void testGetBalanceWithoutBalanceInDB() {
        when(balanceRepository.findBalanceByAccountId(INCORRECT_ACCOUNT_ID)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> balanceService.getBalance(INCORRECT_ACCOUNT_ID));
    }

    @Test
    void testGetBalance() {
        when(balanceRepository.findBalanceByAccountId(ACCOUNT_ID)).thenReturn(Optional.ofNullable(balance));

        BalanceDto actualBalanceDto = balanceService.getBalance(ACCOUNT_ID);
        assertEquals(balanceDto, actualBalanceDto);
    }

    @Test
    void testCreateBalanceWithoutAccountInDB() {
        when(accountRepository.findById(INCORRECT_ACCOUNT_ID)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> balanceService.create(INCORRECT_ACCOUNT_ID));
    }

    @Test
    void testCreateBalance() {
        balance.setCurrentActualBalance(new BigDecimal(0.0));
        balance.setCurrentAuthorizationBalance(new BigDecimal(0.0));
        balance.setCreatedAt(LocalDateTime.now());
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.ofNullable(account));
        when(balanceRepository.save(any())).thenReturn(balance);

        BalanceDto actualDto = balanceService.create(ACCOUNT_ID);

        String actualAccountNumber = actualDto.getAccountNumber();
        Long actualVersion = actualDto.getVersion();
        BigDecimal actualCurrentActualBalance = actualDto.getCurrentActualBalance();
        LocalDateTime actualCreatedAt = actualDto.getCreatedAt().truncatedTo(ChronoUnit.MINUTES);

        verify(balanceRepository).save(any());
        assertEquals(ACCOUNT_NUMBER, actualAccountNumber);
        assertEquals(1L, actualVersion);
        assertEquals(new BigDecimal(0.0), actualCurrentActualBalance);
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), actualCreatedAt);
    }

    @Test
    void testUpdateBalance() {
        balanceDto.setCurrentActualBalance(new BigDecimal(250.0));
        balanceDto.setCurrentAuthorizationBalance(new BigDecimal(200.0));
        when(balanceRepository.findById(1L)).thenReturn(Optional.ofNullable(balance));

        BalanceDto actualDto = balanceService.update(balanceDto);
        BigDecimal actualAuthorizationBalance = actualDto.getCurrentAuthorizationBalance();
        BigDecimal actualBalance = actualDto.getCurrentActualBalance();
        LocalDateTime actualUpdatedAt = actualDto.getUpdatedAt().truncatedTo(ChronoUnit.MINUTES);

        assertEquals(new BigDecimal(200.0), actualAuthorizationBalance);
        assertEquals(new BigDecimal(250.0), actualBalance);
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), actualUpdatedAt);
    }

    @Test
    public void createBalanceAuditTest() {
        Account account = new Account();
        account.setId(1L);
        Balance balance = new Balance();
        balance.setAccount(account);
        balance.setCurrentAuthorizationBalance(new BigDecimal("100.0"));
        balance.setCurrentActualBalance(new BigDecimal("200.0"));

        balanceService = new BalanceService(balanceRepository, balanceAuditRepository, accountRepository, balanceMapper, balanceAuditMapper);
        balanceService.createBalanceAudit(balance);

        verify(balanceAuditRepository).save(any(BalanceAudit.class));
    }

    @Test
    public void getBalanceAuditsTest() {
        BalanceAudit balanceAudit = new BalanceAudit();
        balanceAudit.setId(1L);

        when(balanceAuditRepository.findAllByBalanceId(1L)).thenReturn(List.of(balanceAudit));
        when(balanceAuditMapper.toAuditDto(balanceAudit)).thenReturn(new BalanceAuditDto());

        balanceService = new BalanceService(balanceRepository, balanceAuditRepository, accountRepository, balanceMapper, balanceAuditMapper);
        List<BalanceAuditDto> result = balanceService.getBalanceAudits(1L);

        assertNotNull(result);
    }

    @Test
    public void getBalanceAuditsThrowExceptionTest() {

        when(balanceAuditRepository.findAllByBalanceId(1L)).thenReturn(Collections.emptyList());

        balanceService = new BalanceService(balanceRepository, balanceAuditRepository, accountRepository, balanceMapper, balanceAuditMapper);
        List<BalanceAuditDto> result = balanceService.getBalanceAudits(1L);

        assertTrue(result.isEmpty());
    }
}

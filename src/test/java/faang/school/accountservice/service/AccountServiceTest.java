package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.UpdateBalanceDto;
import faang.school.accountservice.entity.Balance;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.owner.Owner;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.exception.BalanceNotFoundException;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BalanceRepository balanceRepository;
    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @Spy
    private BalanceMapper balanceMapper = Mappers.getMapper(BalanceMapper.class);
    @InjectMocks
    private AccountService accountService;

    private Balance balance;
    private Account account;
    private Long accountNumber;
    private Owner owner;
    private AccountType type;
    private Currency currency;
    private AccountStatus status;
    private UpdateBalanceDto updateBalanceDto;

    @BeforeEach
    void setUp(){
        accountNumber = 1l;
        owner = Owner.builder().id(1L).build();
        type = AccountType.BUSINESS_CHECKING;
        currency = faang.school.accountservice.enums.Currency.EUR;
        status = AccountStatus.ACTIVE;
        account = Account.builder().number(accountNumber).build();
        LocalDateTime now = LocalDateTime.now();
        balance = createBalance(1L, account, now, new BigDecimal(0), new BigDecimal(0), 0L);
        updateBalanceDto = createUpdateDto(1L, new BigDecimal(300));
    }

    @Test
    void createAccount_Successful(){

        lenient().when(accountRepository.save(account)).thenReturn(account);
        lenient().when(balanceRepository.save(balance)).thenReturn(balance);

        AccountDto accountDto = accountService.createAccount(accountNumber, owner, type, currency, status);

        assertNotNull(accountDto);
        assertEquals(account.getId(), accountDto.getId());
        assertEquals(accountNumber, accountDto.getNumber());
    }

    @Test
    void getBalance_BalanceNotFoundException(){
        when(balanceRepository.findById(balance.getId())).thenReturn(Optional.empty());

        BalanceNotFoundException ex = assertThrows(
                BalanceNotFoundException.class, () -> accountService.getBalance(balance.getId()));
        assertEquals(ex.getMessage(), MessageFormat.format("Balance {0} not found", balance.getId()));
    }

    @Test
    void getBalance_Successful(){
        when(balanceRepository.findById(balance.getId())).thenReturn(Optional.of(balance));
        BalanceDto expectedBalanceDto = balanceMapper.toDto(balance);

        BalanceDto actualBalanceDto = accountService.getBalance(balance.getId());

        assertEquals(expectedBalanceDto, actualBalanceDto);
    }

    @Test
    void updateBalance_Successful(){
        lenient().when(balanceRepository.findById(balance.getId())).thenReturn(Optional.of(balance));

        BalanceDto expectedBalanceDto = balanceMapper.toDto(balance);

        expectedBalanceDto.setCurrentBalance(updateBalanceDto.getDeposit());

        BalanceDto resultBalanceDto = accountService.updateBalance(updateBalanceDto);

        assertEquals(expectedBalanceDto.getCurrentBalance(), resultBalanceDto.getCurrentBalance());
        assertEquals(expectedBalanceDto.getBalanceVersion() + 1, resultBalanceDto.getBalanceVersion());
    }

    @Test
    void updateBalance_NotFound(){
        when(balanceRepository.findById(balance.getId())).thenReturn(Optional.empty());

        BalanceNotFoundException ex = assertThrows(
                BalanceNotFoundException.class, () -> accountService.updateBalance(updateBalanceDto));
        assertEquals(ex.getMessage(), MessageFormat.format("Balance {0} not found", balance.getId()));
    }

    private Balance createBalance(Long id, Account account, LocalDateTime time, BigDecimal authBalance, BigDecimal currentBlance,
                                  Long balanceVersion){
        return Balance.builder()
                .id(id)
                .account(account)
                .authorizationBalance(authBalance)
                .currentBalance(currentBlance)
                .created(time)
                .updated(time)
                .balanceVersion(balanceVersion)
                .build();
    }
    private UpdateBalanceDto createUpdateDto(Long balanceId, BigDecimal deposit){
        return UpdateBalanceDto.builder().balanceId(balanceId).deposit(deposit).build();
    }
}

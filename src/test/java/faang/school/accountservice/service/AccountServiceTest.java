package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountCreationRequest;
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
import faang.school.accountservice.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private FreeAccountNumberService freeAccountNumberService;
    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @Spy
    private BalanceMapper balanceMapper = Mappers.getMapper(BalanceMapper.class);
    @InjectMocks
    private AccountService accountService;

    private Balance balance;
    private Account account;
    private String accountNumber;
    private Owner owner;
    private AccountType type;
    private Currency currency;
    private AccountStatus status;
    private UpdateBalanceDto updateBalanceDto;

    private AccountCreationRequest accountCreationRequest;

    @BeforeEach
    void setUp(){
        accountNumber = "999900000000000000000001";
        owner = Owner.builder().id(1L).build();
        type = AccountType.BUSINESS_CHECKING;
        currency = faang.school.accountservice.enums.Currency.EUR;
        status = AccountStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        balance = createBalance(1L, account, now, new BigDecimal(0), new BigDecimal(0), 0L);
        accountCreationRequest = createAccountRequestDto(owner.getId(), type, currency, status);
        account = createAccount(1L, new BigInteger(accountNumber), owner, type, currency, status);
        updateBalanceDto = createUpdateDto(1L, new BigDecimal(300));
    }

    @Test
    void createAccount_Successful(){

        when(ownerRepository.getById(accountCreationRequest.getOwnerId())).thenReturn(owner);
        when(freeAccountNumberService.getFreeNumber(any(), any())).thenReturn(new BigInteger(accountNumber));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);

        AccountDto accountDto = accountService.createAccount(accountCreationRequest);

        assertNotNull(accountDto);
        assertEquals(account.getId(), accountDto.getId());
        assertEquals(accountNumber, accountDto.getNumber().toString());
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
    private AccountCreationRequest createAccountRequestDto(Long ownerId, AccountType type, Currency currency,
                                                           AccountStatus status){
        return AccountCreationRequest.builder().ownerId(ownerId).type(type).currency(currency).status(status).build();
    }
    private Account createAccount (Long id, BigInteger number, Owner owner, AccountType type, Currency currency,
                                   AccountStatus status){
        return Account.builder()
                .id(1L)
                .number(number)
                .owner(owner)
                .type(type)
                .currency(currency)
                .status(status)
                .build();
    }
}

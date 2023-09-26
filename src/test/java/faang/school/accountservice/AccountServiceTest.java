package faang.school.accountservice;

import faang.school.accountservice.dto.AccountRequestDto;
import faang.school.accountservice.dto.AccountResponseDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.mapper.AccountRequestMapper;
import faang.school.accountservice.mapper.AccountResponseMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private AccountRequestDto accountRequestDto;
    private AccountResponseDto accountResponseDto;
    private AccountRequestMapper accountRequestMapper;
    private AccountResponseMapper accountResponseMapper;
    private AccountService accountService;
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        accountRequestMapper = Mockito.mock(AccountRequestMapper.class);
        accountResponseMapper = Mockito.mock(AccountResponseMapper.class);
        accountService = new AccountService(accountRepository, accountRequestMapper, accountResponseMapper);
        accountRequestDto = AccountRequestDto.builder()
                .ownerType(OwnerType.USER)
                .accountType(AccountType.CREDIT)
                .currency(Currency.USD)
                .status(AccountStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();
        accountResponseDto = AccountResponseDto.builder()
                .ownerId(1L)
                .accountNumber("123")
                .version(1)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void getAccountTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountResponseMapper.accountToResponseDto(account)).thenReturn(accountResponseDto);
        AccountResponseDto result = accountService.getAccount(1L);
        assertEquals(accountResponseDto, result);
    }

    @Test
    public void openAccountTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountResponseMapper.accountToResponseDto(account)).thenReturn(accountResponseDto);
        when(accountRequestMapper.accountDtoToAccount(accountRequestDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        AccountResponseDto result = accountService.openAccount(accountRequestDto);
        assertEquals(accountResponseDto, result);
        assertEquals(account.getVersion() + 1, result.getVersion());
    }

    @Test
    public void blockAccountTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountResponseMapper.accountToResponseDto(account)).thenReturn(accountResponseDto);
        AccountResponseDto result = accountService.blockAccount(1L);
        assertEquals(AccountStatus.BLOCKED, account.getStatus());
        assertEquals(account.getVersion(), result.getVersion());
    }

    @Test
    public void closeAccountTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountResponseMapper.accountToResponseDto(account)).thenReturn(accountResponseDto);
        AccountResponseDto result = accountService.closeAccount(1L);
        assertEquals(AccountStatus.CLOSED, account.getStatus());
        assertEquals(account.getVersion(), result.getVersion());
    }

    @Test
    public void unlockAccountTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountResponseMapper.accountToResponseDto(account)).thenReturn(accountResponseDto);
        AccountResponseDto result = accountService.unlockAccount(1L);
        assertEquals(AccountStatus.OPEN, account.getStatus());
        assertEquals(account.getVersion(), result.getVersion());
    }

    @Test
    void findAccountByOwnerIdAndOwnerTypeTest() {
        Account account = Account.builder()
                .id(2)
                .ownerType(OwnerType.USER)
                .ownerId(2)
                .accountType(AccountType.SAVINGS)
                .currency(Currency.USD)
                .status(AccountStatus.OPEN)
                .version(1)
                .build();

        when(accountRepository.findAccountByOwnerIdAndOwnerType(2, OwnerType.USER.name()))
                .thenReturn(Optional.of(account));

        Account result = accountService.findAccountByOwnerIdAndOwnerType(2, OwnerType.USER);

        assertEquals(account, result);
        verify(accountRepository).findAccountByOwnerIdAndOwnerType(2, OwnerType.USER.name());
    }

    @Test
    void findAccountByOwnerIdAndOwnerTypeThrowExceptionTest() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> accountService.findAccountByOwnerIdAndOwnerType(2, OwnerType.USER));

        assertEquals("USER with id: 2, does not have an account", exception.getMessage());
    }
}

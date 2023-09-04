package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.Currency;
import faang.school.accountservice.excpetion.EntityNotFoundException;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private OwnerService ownerService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    private Account account;
    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0);

        Owner owner = Owner.builder()
                .id(1L)
                .build();

        account = Account.builder()
                .id(1L)
                .accountNumber("123")
                .owner(owner)
                .accountType(AccountType.CHECKING_ACCOUNT)
                .currency(Currency.USD)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .build();

        accountDto = AccountDto.builder()
                .id(1L)
                .accountNumber("123")
                .ownerId(1L)
                .accountType(AccountType.CHECKING_ACCOUNT)
                .currency(Currency.USD)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .build();

        when(ownerService.getOwner(1L)).thenReturn(owner);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);
        when(accountMapper.toEntity(accountDto)).thenReturn(account);
    }

    @Test
    void get_shouldReturnAccount() {
        AccountDto actual = accountService.get(1L);
        assertEquals(accountDto, actual);
    }

    @Test
    void open_shouldInvokeMapperToEntity() {
        accountService.open(accountDto);
        verify(accountMapper).toEntity(accountDto);
    }

    @Test
    void open_shouldInvokeOwnerServiceGetOwner() {
        accountService.open(accountDto);
        verify(ownerService).getOwner(1L);
    }

    @Test
    void open_shouldInvokeRepositorySave() {
        accountService.open(accountDto);
        verify(accountRepository).save(account);
    }

    @Test
    void open_shouldInvokeMapperToDto() {
        accountService.open(accountDto);
        verify(accountMapper).toDto(account);
    }

    @Test
    void block_shouldInvokeRepositoryFindById() {
        accountService.block(1L);
        verify(accountRepository).findById(1L);
    }

    @Test
    void block_shouldReturnBlockedAccount() {
        AccountDto actual = accountService.block(1L);
        accountDto.setAccountStatus(AccountStatus.BLOCKED);
        assertEquals(accountDto, actual);
    }

    @Test
    void close_shouldInvokeRepositoryFindById() {
        accountService.close(1L);
        verify(accountRepository).findById(1L);
    }

    @Test
    void close_shouldReturnBlockedAccount() {
        AccountDto actual = accountService.close(1L);
        accountDto.setAccountStatus(AccountStatus.CLOSED);
        assertEquals(accountDto, actual);
    }

    @Test
    void getAccount_shouldInvokeRepositoryFindById() {
        accountService.getAccount(1L);
        verify(accountRepository).findById(1L);
    }

    @Test
    void getAccount_shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> accountService.getAccount(2L),
                "Account with id 2 not found");
    }
}
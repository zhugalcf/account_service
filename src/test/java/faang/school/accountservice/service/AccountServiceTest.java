package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.mapper.AccountMapperImpl;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.AccountStatus;
import faang.school.accountservice.model.AccountType;
import faang.school.accountservice.model.Currency;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.util.exception.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountMapperImpl accountMapper;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void get_AccountNotFound_ShouldThrowException() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException e = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            accountService.get(1L);
        });
        Assertions.assertEquals("Account with id 1 not found", e.getMessage());
    }

    @Test
    void get_AccountFound_ShouldReturnCorrectDto() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount()));

        Assertions.assertEquals(mockAccountDto(), accountService.get(1L));
    }

    private Account mockAccount() {
        return Account.builder()
                .id(1L)
                .userId(1L)
                .number("123")
                .status(AccountStatus.ACTIVE)
                .type(AccountType.CURRENT_ACCOUNT)
                .currency(Currency.USD)
                .version(1L)
                .build();
    }

    private AccountDto mockAccountDto() {
        return AccountDto.builder()
                .id(1L)
                .userId(1L)
                .number("123")
                .status(AccountStatus.ACTIVE)
                .type(AccountType.CURRENT_ACCOUNT)
                .currency(Currency.USD)
                .version(1L)
                .build();
    }
}

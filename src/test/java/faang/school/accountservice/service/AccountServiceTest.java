package faang.school.accountservice.service;

import faang.school.accountservice.client.ProjectServiceClient;
import faang.school.accountservice.client.UserServiceClient;
import faang.school.accountservice.dto.account.AccountDto;
import faang.school.accountservice.dto.project.ProjectDto;
import faang.school.accountservice.dto.user.UserDto;
import faang.school.accountservice.mapper.AccountMapperImpl;
import faang.school.accountservice.messaging.EventService;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.AccountStatus;
import faang.school.accountservice.model.AccountType;
import faang.school.accountservice.model.Currency;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.util.exception.DataValidationException;
import faang.school.accountservice.util.exception.EntityNotFoundException;
import faang.school.accountservice.util.validator.AccountServiceValidator;
import feign.FeignException;
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

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ProjectServiceClient projectServiceClient;

    @Mock
    private EventService eventService;

    @Spy
    private AccountServiceValidator accountServiceValidator = new AccountServiceValidator(userServiceClient, projectServiceClient);

    @InjectMocks
    private AccountService accountService;
    
    private static final Long ID = 1L;

    @Test
    void get_AccountNotFound_ShouldThrowException() {
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.empty());

        EntityNotFoundException e = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            accountService.get(ID);
        });
        Assertions.assertEquals(String.format("Account with id %s not found", ID), e.getMessage());
    }

    @Test
    void get_AccountFound_ShouldReturnCorrectDto() {
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.of(mockAccount()));

        Assertions.assertEquals(mockAccountDto(), accountService.get(ID));
    }

    @Test
    void create_RequestHasUserAndProject_ShouldThrowException() {
        AccountDto accountDto = mockAccountDto();
        accountDto.setProjectId(ID);

        DataValidationException e = Assertions.assertThrows(DataValidationException.class, () -> {
            accountService.create(accountDto);
        });
        Assertions.assertEquals("Post's author can be only author or project and can't be both", e.getMessage());
    }

    @Test
    void create_RequestHasNoOwners_ShouldThrowException() {
        AccountDto accountDto = mockAccountDto();
        accountDto.setUserId(null);

        DataValidationException e = Assertions.assertThrows(DataValidationException.class, () -> {
            accountService.create(accountDto);
        });
        Assertions.assertEquals("Post's author can be only author or project and can't be both", e.getMessage());
    }

    @Test
    void create_RequestHasOnlyOneOwner_ShouldMapCorrectlyAndSave() {
        Mockito.doNothing().when(accountServiceValidator).validateToCreate(mockAccountDto());
        Mockito.doNothing().when(eventService).publish(Mockito.any(), Mockito.any());

        accountService.create(mockAccountDto());

        Mockito.verify(accountRepository).save(mockAccount());
    }

    @Test
    void freeze_AccountNotFound_ShouldThrowException() {
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.empty());

        EntityNotFoundException e = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            accountService.freeze(ID);
        });
        Assertions.assertEquals(String.format("Account with id %s not found", ID), e.getMessage());
    }

    @Test
    void freeze_AccountIsFrozen_ShouldThrowException() {
        Account account = mockAccount();
        account.setStatus(AccountStatus.FROZEN);
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.of(account));

        DataValidationException e = Assertions.assertThrows(DataValidationException.class, () -> {
            accountService.freeze(ID);
        });
        Assertions.assertEquals(String.format("Account with id %s is already frozen", ID), e.getMessage());
    }

    @Test
    void freeze_AccountIsClosed_ShouldThrowException() {
        Account account = mockAccount();
        account.setStatus(AccountStatus.CLOSED);
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.of(account));

        DataValidationException e = Assertions.assertThrows(DataValidationException.class, () -> {
            accountService.freeze(ID);
        });
        Assertions.assertEquals(String.format("Account with id %s is closed", ID), e.getMessage());
    }

    @Test
    void freeze_InputsAreCorrect_StatusShouldBeFrozenAndAccountShouldBeSaved() {
        Account account = mockAccount();
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.of(account));

        accountService.freeze(ID);

        Assertions.assertEquals(AccountStatus.FROZEN, account.getStatus());
        Mockito.verify(accountRepository).save(account);
    }

    @Test
    void close_AccountNotFound_ShouldThrowException() {
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.empty());

        EntityNotFoundException e = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            accountService.close(ID);
        });
        Assertions.assertEquals(String.format("Account with id %s not found", ID), e.getMessage());
    }

    @Test
    void close_AccountIsClosed_ShouldThrowException() {
        Account account = mockAccount();
        account.setStatus(AccountStatus.CLOSED);
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.of(account));

        DataValidationException e = Assertions.assertThrows(DataValidationException.class, () -> {
            accountService.close(ID);
        });
        Assertions.assertEquals(String.format("Account with id %s is already closed", ID), e.getMessage());
    }

    @Test
    void close_InputsAreCorrect_StatusShouldBeClosedAndAccountShouldBeSaved() {
        Account account = mockAccount();
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.of(account));

        accountService.close(ID);

        Assertions.assertEquals(AccountStatus.CLOSED, account.getStatus());
        Mockito.verify(accountRepository).save(account);
    }

    private Account mockAccount() {
        return Account.builder()
                .id(ID)
                .userId(ID)
                .number("123456789012345")
                .status(AccountStatus.ACTIVE)
                .type(AccountType.CURRENT_ACCOUNT)
                .currency(Currency.USD)
                .version(ID)
                .build();
    }

    private AccountDto mockAccountDto() {
        return AccountDto.builder()
                .id(ID)
                .userId(ID)
                .number("123456789012345")
                .status(AccountStatus.ACTIVE)
                .type(AccountType.CURRENT_ACCOUNT)
                .currency(Currency.USD)
                .version(ID)
                .build();
    }
}

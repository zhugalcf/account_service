package faang.school.accountservice;

import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.exception.DataValidationException;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.mapper.AccountRequestMapper;
import faang.school.accountservice.mapper.AccountResponseMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountThrowExceptionTest {

    private AccountService accountService;
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        AccountRequestMapper accountRequestMapper = Mockito.mock(AccountRequestMapper.class);
        AccountResponseMapper accountResponseMapper = Mockito.mock(AccountResponseMapper.class);
        accountService = new AccountService(accountRepository, accountRequestMapper, accountResponseMapper);
    }

    @Test
    public void getAccountByIdThrowExceptionTest() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> accountService.getAccount(1L));
    }

    @Test
    public void saveAccountAfterBlockThrowExceptionTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenThrow(OptimisticLockingFailureException.class);
        assertThrows(IllegalArgumentException.class, () -> accountService.blockAccount(account.getId()));
    }

    @Test
    public void saveAccountAfterCloseThrowExceptionTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenThrow(OptimisticLockingFailureException.class);
        assertThrows(IllegalArgumentException.class, () -> accountService.closeAccount(account.getId()));
    }

    @Test
    public void saveAccountAfterUnlockThrowExceptionTest() {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenThrow(OptimisticLockingFailureException.class);
        assertThrows(IllegalArgumentException.class, () -> accountService.unlockAccount(account.getId()));
    }

    @Test
    public void checkAccountBlockedStatusTest() {
        Account account = new Account();
        account.setStatus(AccountStatus.BLOCKED);
        assertThrows(DataValidationException.class, () -> accountService.checkAccountBlockedStatus(account));
    }

    @Test
    public void checkAccountClosedStatusTest() {
        Account account = new Account();
        account.setStatus(AccountStatus.CLOSED);
        assertThrows(DataValidationException.class, () -> accountService.checkAccountClosedStatus(account));
    }
}

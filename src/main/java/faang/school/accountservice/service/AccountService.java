package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountRequestDto;
import faang.school.accountservice.dto.AccountResponseDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.exception.DataValidationException;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.mapper.AccountRequestMapper;
import faang.school.accountservice.mapper.AccountResponseMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountRequestMapper accountRequestMapper;
    private final AccountResponseMapper accountResponseMapper;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public AccountResponseDto getAccount(long accountId) {
        checkAccountClosedStatus(getAccountById(accountId));
        checkAccountBlockedStatus(getAccountById(accountId));
        Account account = getAccountById(accountId);
        return accountResponseMapper.accountToResponseDto(account);
    }

    @Transactional
    public AccountResponseDto openAccount(AccountRequestDto accountDto) {
        Account account = accountRequestMapper.accountDtoToAccount(accountDto);
        accountRepository.save(account);
        log.info("Account created by id: {}, at: {}",
                account.getId(), account.getCreatedAt());
        return accountResponseMapper.accountToResponseDto(account);
    }

    @Transactional
    public AccountResponseDto blockAccount(long accountId) {
        checkAccountBlockedStatus(getAccountById(accountId));
        Account account = getAccountById(accountId);
        int version = account.getVersion();
        log.info("Account number: {}, is blocked!", account.getNumber());
        account.setStatus(AccountStatus.BLOCKED);
        account.setVersion(version + 1);
        saveAccountAfterBlock(account);
        return accountResponseMapper.accountToResponseDto(account);
    }

    @Transactional
    public AccountResponseDto closeAccount(long accountId) {
        checkAccountClosedStatus(getAccountById(accountId));
        Account account = getAccountById(accountId);
        int version = account.getVersion();
        log.info("Account number: {}, is closed!", account.getNumber());
        account.setStatus(AccountStatus.CLOSED);
        account.setVersion(version + 1);
        account.setClosedAt(LocalDateTime.now());
        saveAccountAfterClose(account);
        return accountResponseMapper.accountToResponseDto(account);
    }

    @Transactional
    public AccountResponseDto unlockAccount(long accountId) {
        Account account = getAccountById(accountId);
        if (account.getStatus() != AccountStatus.OPEN) {
            int version = account.getVersion();
            log.info("Account number: {}, is unlocked!", account.getNumber());
            account.setStatus(AccountStatus.OPEN);
            account.setVersion(version + 1);
            saveAccountAfterUnlock(account);
        }
        return accountResponseMapper.accountToResponseDto(account);
    }

    public Account getAccountById(long accountId) {
        log.info("A request to retrieve an account by its ID: {}, has been received.", accountId);
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id " + accountId));
    }

    public Account findAccountByOwnerIdAndOwnerType(long ownerId, OwnerType ownerType) {
        return accountRepository.findAccountByOwnerIdAndOwnerType(ownerId, ownerType.name())
                .orElseThrow(() -> new NotFoundException(String.format("%s with id: %d, does not have an account", ownerType, ownerId)));
    }

    private void saveAccountAfterBlock(Account account) {
        try {
            accountRepository.save(account);
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalArgumentException("Account is blocked by another transaction");
        }
    }

    private void saveAccountAfterClose(Account account) {
        try {
            accountRepository.save(account);
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalArgumentException("Account is closed by another transaction");
        }
    }

    private void saveAccountAfterUnlock(Account account) {
        try {
            accountRepository.save(account);
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalArgumentException("Account is already opened");
        }
    }

    public void checkAccountBlockedStatus(Account account) {
        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new DataValidationException("Account is blocked already");
        }
    }

    public void checkAccountClosedStatus(Account account) {
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new DataValidationException("Account is closed already");
        }
    }
}

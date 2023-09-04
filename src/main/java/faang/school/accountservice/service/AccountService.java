package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public AccountDto getAccount(long accountId) {
        Account account = getAccountById(accountId);
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto openAccount(AccountDto accountDto) {
        Account account = accountMapper.accountDtoToAccount(accountDto);
        accountRepository.save(account);
        log.info("Account with number: {}, created by id: {}, at: {}",
                accountDto.getAccountNumber(), accountDto.getId(), accountDto.getCreatedAt());
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto blockAccount(long accountId) {
        Account account = getAccountById(accountId);
        int version = account.getVersion();
        log.info("Account number: {}, is blocked!", account.getNumber());
        account.setStatus(AccountStatus.BLOCKED);
        account.setVersion(version + 1);
        saveAccountAfterBlock(account);
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto closeAccount(long accountId) {
        Account account = getAccountById(accountId);
        int version = account.getVersion();
        log.info("Account number: {}, is closed!", account.getNumber());
        account.setStatus(AccountStatus.CLOSED);
        account.setVersion(version + 1);
        saveAccountAfterClose(account);
        return accountMapper.accountToAccountDto(account);
    }

    private Account getAccountById(long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id " + accountId));
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
}

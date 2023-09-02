package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.enums.AccountStatus;
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
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found " + accountId));
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto openAccount(AccountDto accountDto) {
        Account account = accountMapper.accountDtoToAccount(accountDto);
        account.setVersion(0);
        accountRepository.save(account);
        log.info("Account with number: {}, created by id: {}, at: {}",
                accountDto.getAccountNumber(), accountDto.getId(), accountDto.getCreatedAt());
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto blockAccount(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found + " + accountId));
        int version = account.getVersion();
        log.info("Account number: {}, is blocked!", account.getNumber());
        account.setStatus(AccountStatus.BLOCKED);
        account.setVersion(version + 1);
        try {
            accountRepository.save(account);
            return accountMapper.accountToAccountDto(account);
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalArgumentException("Account is blocked by another transaction");
        }
    }

    @Transactional
    public AccountDto closeAccount(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found " + accountId));
        int version = account.getVersion();
        log.info("Account number: {}, is closed!", account.getNumber());
        account.setStatus(AccountStatus.CLOSED);
        account.setVersion(version + 1);
        try {
            accountRepository.save(account);
            return accountMapper.accountToAccountDto(account);
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalArgumentException("Account is closed by another transaction");
        }
    }
}

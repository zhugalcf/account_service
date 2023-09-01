package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDto getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setVersion(account.getVersion() + 1);
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto openAccount(AccountDto accountDto) {
        Account account = accountMapper.accountDtoToAccount(accountDto);
        accountRepository.save(account);
        log.info("Account with number: {}, created by id: {}, at: {}",
                accountDto.getAccountNumber(), accountDto.getId(), accountDto.getCreatedAt());
        account.setVersion(account.getVersion() + 1);
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto freezeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        log.info("Account number: {}, is frozen!", account.getNumber());
        account.setVersion(account.getVersion() + 1);
        account.setStatus(AccountStatus.FROZEN);
        accountRepository.save(account);
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto blockAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        log.info("Account number: {}, is blocked!", account.getNumber());
        account.setVersion(account.getVersion() + 1);
        account.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(account);
        return accountMapper.accountToAccountDto(account);
    }

    @Transactional
    public AccountDto closeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        log.info("Account number: {}, is closed!", account.getNumber());
        account.setVersion(account.getVersion() + 1);
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        return accountMapper.accountToAccountDto(account);
    }
}

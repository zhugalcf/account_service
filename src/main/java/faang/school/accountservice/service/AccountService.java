package faang.school.accountservice.service;

import faang.school.accountservice.dto.account.AccountDto;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.AccountStatus;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.util.exception.EntityNotFoundException;
import faang.school.accountservice.util.validator.AccountServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountServiceValidator accountServiceValidator;

    public AccountDto get(Long id) {
        Account foundAccount = getAccountById(id);

        return accountMapper.toDto(foundAccount);
    }

    @Transactional
    public AccountDto create(AccountDto dto) {
        accountServiceValidator.validateToCreate(dto);

        Account account = accountMapper.toEntity(dto);

        Account saved = accountRepository.save(account); // TODO: 06.09.2023 BC-5752

        log.info("Created account: {}", saved);

        return accountMapper.toDto(saved);
    }

    @Transactional
    public AccountDto freeze(Long id) {
        Account foundAccount = getAccountById(id);

        foundAccount.setStatus(AccountStatus.FROZEN);

        Account saved = accountRepository.save(foundAccount);

        log.info("Account was frozen: {}", saved);

        return accountMapper.toDto(saved);
    }

    @Transactional
    public AccountDto block(Long id) {
        Account foundAccount = getAccountById(id);

        foundAccount.setStatus(AccountStatus.BLOCKED);

        Account saved = accountRepository.save(foundAccount);

        log.info("Account was blocked: {}", saved);

        return accountMapper.toDto(saved);
    }

    @Transactional
    public AccountDto close(Long id) {
        Account foundAccount = getAccountById(id);

        foundAccount.setStatus(AccountStatus.CLOSED);
        foundAccount.setClosedAt(Instant.now());

        Account saved = accountRepository.save(foundAccount);

        log.info("Account was closed: {}", saved);

        return accountMapper.toDto(saved);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account with id %s not found", id)));
    }
}

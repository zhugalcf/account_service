package faang.school.accountservice.service;

import faang.school.accountservice.dto.account.AccountDto;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.util.exception.EntityNotFoundException;
import faang.school.accountservice.util.validator.AccountServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountOwnerChecker accountOwnerChecker;

    public AccountDto get(Long id) {
        Account foundAccount = getAccountById(id);

        return accountMapper.toDto(foundAccount);
    }

    @Transactional
    public AccountDto create(AccountDto dto) {
        accountOwnerChecker.validateToCreate(dto);

        Account account = accountMapper.toEntity(dto);

        Account saved = accountRepository.save(account); // TODO: 06.09.2023 BC-5752

        return accountMapper.toDto(saved);
    }

    @Transactional
    public AccountDto freeze(Long id) {
        Account foundAccount = getAccountById(id);

        accountServiceValidator.validateToFreeze(foundAccount);

        log.info("Freezing account: {}", foundAccount);

        foundAccount.setStatus(AccountStatus.FROZEN);

        Account saved = accountRepository.save(foundAccount);

        return accountMapper.toDto(saved);
    }

    @Transactional
    public AccountDto close(Long id) {
        Account foundAccount = getAccountById(id);

        accountServiceValidator.validateToClose(foundAccount);

        log.info("Closing account: {}", foundAccount);

        foundAccount.setStatus(AccountStatus.CLOSED);
        foundAccount.setClosedAt(LocalDateTime.now());

        Account saved = accountRepository.save(foundAccount);

        return accountMapper.toDto(saved);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account with id %s not found", id)));
    }
}

package faang.school.accountservice.service;

import faang.school.accountservice.dto.account.AccountClosingEvent;
import faang.school.accountservice.dto.account.AccountCreationEvent;
import faang.school.accountservice.dto.account.AccountDto;
import faang.school.accountservice.dto.account.AccountFreezingEvent;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.messaging.EventService;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.AccountStatus;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.util.exception.EntityNotFoundException;
import faang.school.accountservice.util.validator.AccountServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountServiceValidator accountServiceValidator;
    private final EventService eventService;

    public AccountDto get(Long id) {
        Account foundAccount = getAccountById(id);

        return accountMapper.toDto(foundAccount);
    }

    @Transactional
    public AccountDto create(AccountDto dto) {
        accountServiceValidator.validateToCreate(dto);

        Account account = accountMapper.toEntity(dto);

        log.info("Creating new account: {}", account);

        Account saved = accountRepository.save(account); // тут еще нужно будет сетить сгенерированный номер счета из другой таски

        eventService.publish(saved, AccountCreationEvent.class);

        return accountMapper.toDto(saved);
    }

    @Transactional
    public AccountDto freeze(Long id) {
        Account foundAccount = getAccountById(id);

        accountServiceValidator.validateToFreeze(foundAccount);

        log.info("Freezing account: {}", foundAccount);

        foundAccount.setStatus(AccountStatus.FROZEN);

        Account saved = accountRepository.save(foundAccount);

        eventService.publish(saved, AccountFreezingEvent.class);

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

        eventService.publish(saved, AccountClosingEvent.class);

        return accountMapper.toDto(saved);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account with id %s not found", id)));
    }
}

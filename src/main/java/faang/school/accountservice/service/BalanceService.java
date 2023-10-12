package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.mapper.BalanceAuditMapper;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.BalanceAudit;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceAuditRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceAuditRepository balanceAuditRepository;
    private final AccountRepository accountRepository;
    private final BalanceMapper balanceMapper;
    private final BalanceAuditMapper balanceAuditMapper;

    @Transactional(readOnly = true)
    public BalanceDto getBalance(long accountId) {
        Balance balance = balanceRepository.findBalanceByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Entity balance not found"));
        log.info("Balance with id = {} has taken from DB successfully", balance.getId());
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto create(long accountId) {
        Account account = getAccount(accountId);

        Balance balance = Balance.builder()
                .account(account)
                .currentAuthorizationBalance(new BigDecimal(0.0))
                .currentActualBalance(new BigDecimal(0.0))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1L)
                .build();

        Balance savedBalance = balanceRepository.save(balance);
        createBalanceAudit(balance);
        log.info("New balance with account number ={} was created successfully", account.getNumber());
        return balanceMapper.toDto(savedBalance);
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockException.class, maxAttempts = 3, backoff = @Backoff(delay = 300))
    public BalanceDto update(BalanceDto balanceDto) {
        Balance balance = balanceRepository.findById(balanceDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Entity balance not found"));

        balance.setCurrentAuthorizationBalance(balanceDto.getCurrentAuthorizationBalance());
        balance.setCurrentActualBalance(balanceDto.getCurrentActualBalance());
        balance.setUpdatedAt(LocalDateTime.now());
        log.info("Balance with account number = {} was updated successfully", balanceDto.getAccountNumber());
        return balanceMapper.toDto(balance);
    }

    public void createBalanceAudit(Balance balance) {
        BalanceAudit balanceAudit = new BalanceAudit();
        balanceAudit.setBalance(balance);
        balanceAudit.setVersion(1);
        balanceAudit.setAuthorizationAmount(balance.getCurrentAuthorizationBalance());
        balanceAudit.setActualAmount(balance.getCurrentActualBalance());
        balanceAudit.setOperationId(1L);
        balanceAudit.setAuditTimestamp(LocalDateTime.now());
        balanceAuditRepository.save(balanceAudit);
        log.info("Balance audit was created successfully by balance id: {} ", balance.getId());
    }

    public List<BalanceAuditDto> getBalanceAudits(long balanceId) {
        List<BalanceAudit> balanceAudits = getBalanceAuditsById(balanceId);
        log.info("Balance audit was taken successfully by balance id: {} ", balanceId);
        return balanceAuditMapper.toListAuditDto(balanceAudits);
    }

    private Account getAccount(long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new EntityNotFoundException("Entity account not found"));
    }

    private List<BalanceAudit> getBalanceAuditsById(long balanceId) {
        List<BalanceAudit> audits = balanceAuditRepository.findAllByBalanceId(balanceId);
        if (audits.isEmpty()) {
            return Collections.emptyList();
        }
        return audits;
    }
}

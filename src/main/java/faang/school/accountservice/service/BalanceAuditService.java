package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.mapper.BalanceAuditMapper;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.BalanceAudit;
import faang.school.accountservice.repository.BalanceAuditRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceAuditService {
    private final BalanceAuditRepository balanceAuditRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceAuditMapper balanceAuditMapper;

    @Transactional
    public void createBalanceAudit(long balanceId) {
        Balance balance = balanceRepository.findById(balanceId)
                .orElseThrow(() -> new EntityNotFoundException("Not found balance from id: {}" + balanceId));
        BalanceAudit balanceAudit = new BalanceAudit();
        balanceAudit.setBalance(balance);
        balanceAudit.setVersion(1);
        balanceAudit.setAuthorizationAmount(balance.getCurrentAuthorizationBalance());
        balanceAudit.setActualAmount(balance.getCurrentActualBalance());
        balanceAudit.setOperationId(1L);
        balanceAudit.setAuditTimestamp(LocalDateTime.now());
        balanceAuditRepository.save(balanceAudit);
        log.info("Balance audit was created successfully by balance id: {}", balance.getId());
    }

    public BalanceAuditDto getBalanceAudits(long balanceId) {
        BalanceAudit balanceAudit = getBalanceById(balanceId);
        log.info("Balance audit was taken successfully by balance id: {}", balanceId);
        return balanceAuditMapper.toBalance(balanceAudit);
    }

    private BalanceAudit getBalanceById(long balanceId) {
        return balanceAuditRepository.findById(balanceId)
                .orElseThrow(() -> new EntityNotFoundException("Not found balance audit"));
    }
}

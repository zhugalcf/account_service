package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.mapper.BalanceAuditMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.BalanceAudit;
import faang.school.accountservice.repository.BalanceAuditRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceAuditService {
    private final BalanceRepository balanceRepository;
    private final BalanceAuditRepository balanceAuditRepository;

    private final BalanceAuditMapper mapper;
    @Transactional
    public void snapshotBalanceAudit(Account account){
        Balance balance = balanceRepository.findByAccount(account)
                .orElseThrow(() -> new EntityNotFoundException("Balance not found with account ID: " + account.getId()));
        BalanceAudit balanceAudit = new BalanceAudit();
        balanceAudit.setBalanceVersion(balance.getVersion());
        balanceAudit.setAuthorizationBalance(balance.getAuthorizationBalance());
        balanceAudit.setCurrentBalance(balance.getCurrentBalance());
        balanceAudit.setOperationId(null);
        balanceAudit.setCreatedAt(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        balanceAudit.setAccount(account);
        balanceAuditRepository.save(balanceAudit);
    }

    @Transactional
    public List<BalanceAuditDto> getBalanceAudits(Long accountId){
        List<BalanceAudit> balanceAudits = balanceAuditRepository.findAllByAccountId(accountId);
        return mapper.toDtoList(balanceAudits);
    }
}
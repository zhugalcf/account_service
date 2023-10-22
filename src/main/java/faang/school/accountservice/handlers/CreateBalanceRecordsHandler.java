package faang.school.accountservice.handlers;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.BalanceAudit;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceAuditRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.AbstractCollection;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateBalanceRecordsHandler implements RequestTaskHandler<String, Object> {
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceAuditRepository balanceAuditRepository;

    @Override
    public Map<String, Object> execute(Request request, Map<String, Object> context) {
        Long accountId = (Long) context.get("accountId");
        Account account = createBalance(accountId);
        snapshotBalanceAudit(account);
        return context;
    }

    @Override
    public RequestHandler getHandlerId() {
        return RequestHandler.CREATE_BALANCE_RECORDS;
    }
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
    private Account createBalance(Long accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        checkBalanceIsExist(account);
        Balance balance = new Balance();
        balance.setAccount(account);
        balance.setCurrentBalance(BigDecimal.ZERO);
        balance.setAuthorizationBalance(BigDecimal.ZERO);
        balance.setCreatedAt(ZonedDateTime.now());
        balance.setUpdatedAt(ZonedDateTime.now());
        balance.setVersion(0L);
        balanceRepository.save(balance);
        return  balance.getAccount();
    }
    private void checkBalanceIsExist(Account account) {
        Optional<Balance> existBalance = balanceRepository.findByAccount(account);
        existBalance.ifPresent(balance -> {
            throw new RuntimeException("This account already have balance");
        });
    }
}

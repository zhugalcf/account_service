package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.UpdateBalanceDto;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;
    private final AccountRepository accountRepository;
    private final BalanceAuditService balanceAuditService;

    @Transactional
    public void createBalance(Long accountId) {
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
        balanceAuditService.snapshotBalanceAudit(account);
    }

    @Transactional(readOnly = true)
    public BalanceDto getBalance(Long balanceId) {
        Balance balance = loadBalanceOrThrow(balanceId);

        log.info("Balance with id: {} fetched", balanceId);
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto updateBalance(UpdateBalanceDto updateBalanceDto, Long balanceId) {
        validateOwnership(updateBalanceDto, balanceId);
        Balance balance = loadBalanceOrThrow(updateBalanceDto.getId());

        BigDecimal currentBalance = balance.getCurrentBalance();
        BigDecimal deposit = updateBalanceDto.getDeposit();

        BigDecimal updatedBalance = currentBalance.add(deposit);

        balance.setCurrentBalance(updatedBalance);

        Balance savedBalance = balanceRepository.save(balance);
        balanceAuditService.snapshotBalanceAudit(savedBalance.getAccount());
        log.info("Balance with id: {} updated", balanceId);
        return balanceMapper.toDto(savedBalance);
    }

    private void validateOwnership(UpdateBalanceDto updateBalanceDto, Long balanceId) {
        if (!Objects.equals(balanceId, updateBalanceDto.getId())) {
            throw new IllegalArgumentException("You can only update your own balance");
        }
    }

    private Balance loadBalanceOrThrow(Long balanceId) {
        return balanceRepository.findById(balanceId)
                .orElseThrow(() -> new EntityNotFoundException("Balance with id: " + balanceId + " not found"));
    }
    private void checkBalanceIsExist(Account account) {
        Optional<Balance> existBalance = balanceRepository.findByAccount(account);
        existBalance.ifPresent(balance -> {
            throw new RuntimeException("This account already have balance");
        });
    }
}
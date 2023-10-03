package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.balance.Balance;
import faang.school.accountservice.exception.DuplicateBalanceException;
import faang.school.accountservice.exception.EntityNotFoundException;
import faang.school.accountservice.exception.InsufficientBalanceException;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;
    private final AccountService accountService;

    @Transactional(readOnly = true)
    public BalanceDto getBalanceByAccountId(Long accountId) {
        Balance balance = getBalance(accountId);
        return balanceMapper.toDto(balance);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BalanceDto createBalance(Long accountId, BalanceDto balanceDto) {
        Account account = accountService.getAccount(accountId);

        if (balanceRepository.existsByAccountId(accountId)) {
            throw new DuplicateBalanceException("Balance already exists for account.");
        }

        Balance balance = Balance.builder()
                .authorizationBalance(balanceDto.getAuthorizationBalance())
                .actualBalance(balanceDto.getActualBalance())
                .account(account)
                .build();

        return balanceMapper.toDto(balanceRepository.save(balance));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public BalanceDto updateBalance(Long accountId, BalanceDto balanceDto) {
        Balance balance = getBalance(accountId);
        balanceMapper.updateBalanceFromBalanceDto(balanceDto, balance);

        return balanceMapper.toDto(balanceRepository.save(balance));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public BalanceDto deposit(Long accountId, BigDecimal amount) {
        Balance balance = getBalance(accountId);
        BigDecimal currentAuthorizationBalance = balance.getAuthorizationBalance();
        BigDecimal currentActualBalance = balance.getActualBalance();

        balance.setAuthorizationBalance(currentAuthorizationBalance.add(amount));
        balance.setActualBalance(currentActualBalance.add(amount));

        Balance saved = balanceRepository.save(balance);
        return balanceMapper.toDto(saved);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public BalanceDto withdraw(Long accountId, BigDecimal amount) {
        Balance balance = getBalance(accountId);
        BigDecimal currentAuthorizationBalance = balance.getAuthorizationBalance();

        checkBalanceAmount(currentAuthorizationBalance, amount, "Not enough authorization balance.");

        balance.setAuthorizationBalance(currentAuthorizationBalance.subtract(amount));
        balance.setActualBalance(balance.getActualBalance().subtract(amount));

        Balance saved = balanceRepository.save(balance);
        return balanceMapper.toDto(saved);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public void transfer(Long senderId, Long receiverId, BigDecimal amount) {
        List<Balance> balances = balanceRepository.findAllByAccountIds(Arrays.asList(senderId, receiverId));

        if (balances.size() != 2) {
            throw new EntityNotFoundException("Account balances not found.");
        }

        Balance senderBalance = getFilteredBalance(senderId, balances);
        Balance receiverBalance = getFilteredBalance(receiverId, balances);

        BigDecimal senderAuthorizationBalance = senderBalance.getAuthorizationBalance();
        BigDecimal senderActualBalance = senderBalance.getActualBalance();

        checkBalanceAmount(senderAuthorizationBalance, amount, "Not enough authorization balance to transfer.");

        senderBalance.setAuthorizationBalance(senderAuthorizationBalance.subtract(amount));
        senderBalance.setActualBalance(senderActualBalance.subtract(amount));

        receiverBalance.setActualBalance(receiverBalance.getActualBalance().add(amount));

        balanceRepository.saveAll(Arrays.asList(senderBalance, receiverBalance));
    }

    private void checkBalanceAmount(BigDecimal currentAuthorizationBalance, BigDecimal amount, String message) {
        if (currentAuthorizationBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(message);
        }
    }

    private Balance getFilteredBalance(Long senderId, List<Balance> balances) {
        return balances.stream().filter(b -> b.getAccount().getId().equals(senderId)).findFirst().orElseThrow();
    }

    private Balance getBalance(Long accountId) {
        return balanceRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + accountId + " not found"));
    }
}

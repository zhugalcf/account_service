package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.springframework.transaction.annotation.Transactional;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.mapper.TariffMapper;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.SavingsAccountTariffHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PercentCalculationService {
    private final SavingsAccountService savingsAccountService;
    private final BalanceService balanceService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final SavingsAccountRepository savingsAccountRepository;
    private final SavingsAccountTariffHistoryRepository historyRepository;
    private final TariffMapper tariffMapper;
    private final SavingsAccountMapper savingsAccountMapper;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public void calculateLastUpdatedPercent() {
        log.info("Starting calculation and balance update...");
        List<Long> savingsAccountIds = getAllSavingsAccountIds();

        for (Long savingsAccountId : savingsAccountIds) {
            executorService.submit(() -> {
                try {
                    updateBalance(savingsAccountId);
                    log.info("Interest calculation and balance update completed for account ID: " + savingsAccountId);
                } catch (Exception e) {
                    log.error("Error processing account ID: " + savingsAccountId, e);
                }
            });
        }

        log.info("Interest calculation and balance update started for " + savingsAccountIds.size() + " accounts.");
    }

    @Transactional(readOnly = true)
    public List<Long> getAllSavingsAccountIds() {
        return savingsAccountRepository.findAllSavingsAccountIds();
    }

    private void updateBalance(Long savingsAccountId) {
        try {
            // Получаем id накопительного счета по id аккаунта
            Long savingsId = savingsAccountService.getSavingsAccountIdByAccountId(savingsAccountId);

            // Получите текущий баланс сберегательного счета
            BalanceDto currentBalance = balanceService.getBalanceByAccountId(savingsId);

            // Можно реализовать вашу логику расчета процентов здесь
            // Например, предположим, что процентная ставка равна 0.05 (5%)
            BigDecimal interestRate = new BigDecimal("0.05");

            // Вычислите проценты на основе текущего баланса и процентной ставки
            BigDecimal interest = currentBalance.multiply(interestRate);

            // Обновите баланс с учетом начисленных процентов
            BigDecimal newBalance = currentBalance.add(interest);
            balanceService.updateBalance(savingsId, newBalance);

            log.info("Interest calculation and balance update completed for account ID: " + savingsAccountId);
        } catch (Exception e) {
            log.error("Error updating balance with interest for account ID: " + savingsAccountId, e);
        }
    }

    @Async
    @Transactional
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public void calculateAndApplyLastPercents() {
        List<SavingsAccount> savingsAccounts = savingsAccountRepository.findAllByAccountStatus(AccountStatus.OPENED);

        for (SavingsAccount savingsAccount : savingsAccounts) {
            // Ваш код для расчета процентов и обновления баланса
            // Можно использовать дату последнего успешного начисления процентов для определения периода расчета
            // ...

            // Обновление даты последнего успешного начисления процентов
            savingsAccount.setLastUpdateCalculationAt(LocalDateTime.now());
        }
    }

}

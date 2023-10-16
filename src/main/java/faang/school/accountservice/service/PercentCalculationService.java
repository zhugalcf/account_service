package faang.school.accountservice.service;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.account.SavingsAccount;
import faang.school.accountservice.repository.SavingsAccountTariffHistoryRepository;
import org.springframework.transaction.annotation.Transactional;
import faang.school.accountservice.repository.SavingsAccountRepository;
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
    private final BalanceService balanceService;
    private final SavingsAccountRepository savingsAccountRepository;
    private final SavingsAccountTariffHistoryRepository historyRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public void calculateInterestAndUpdateBalances() {
        List<SavingsAccount> savingsAccounts = savingsAccountRepository.findAll();

        for (SavingsAccount savingsAccount : savingsAccounts) {
            try {
                if (savingsAccount.getLastUpdateCalculationAt() == null ||
                        savingsAccount.getLastUpdateCalculationAt().plusDays(1).isBefore(LocalDateTime.now())) {

                    executorService.submit(() -> {
                        try {
                            TariffDto tariffDto = historyRepository.getCurrentTariffAndRateByClientId(savingsAccount.getId());
                            if (tariffDto != null) {
                                BigDecimal interestRate = tariffDto.getRate();

                                BigDecimal currentBalance = balanceService.getBalanceByAccountId(savingsAccount.getAccount().getId()).getActualBalance();
                                BigDecimal interest = currentBalance.multiply(interestRate);

                                balanceService.deposit(savingsAccount.getAccount().getId(), interest);

                                savingsAccount.setLastUpdateCalculationAt(LocalDateTime.now());
                                savingsAccountRepository.save(savingsAccount);
                                log.info("Interest calculated and deposited for savings account ID: " + savingsAccount.getId());
                            } else {
                                log.warn("No tariff information found for savings account ID: " + savingsAccount.getId());
                            }
                        } catch (Exception e) {
                            log.error("Error processing savings account ID: " + savingsAccount.getId(), e);
                        }
                    });
                }
            } catch (Exception e) {
                log.error("Error processing savings account ID: " + savingsAccount.getId(), e);
            }
        }
        log.info("Calculation and balance update started for " + savingsAccounts.size() + " savings accounts");
        executorService.shutdown();
    }
}
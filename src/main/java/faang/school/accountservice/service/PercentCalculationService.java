//package faang.school.accountservice.service;
//
//import org.springframework.transaction.annotation.Transactional;
//import faang.school.accountservice.mapper.SavingsAccountMapper;
//import faang.school.accountservice.mapper.TariffMapper;
//import faang.school.accountservice.repository.SavingsAccountRepository;
//import faang.school.accountservice.repository.SavingsAccountTariffHistoryRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.OptimisticLockingFailureException;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PercentCalculationService {
//
//    private final SavingsAccountService savingsAccountService;
//
//    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
//
//    private final SavingsAccountRepository savingsAccountRepository;
//    private final SavingsAccountTariffHistoryRepository historyRepository;
//    private final TariffMapper tariffMapper;
//    private final SavingsAccountMapper savingsAccountMapper;
//
//    @Async
//    @Transactional
//    @Retryable(retryFor = {OptimisticLockingFailureException.class})
//    public void percentCalculateAndApply() {
//
//    }
//
//    @Async
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Retryable(retryFor = {OptimisticLockingFailureException.class})
//    public void calculateAndApplyLastPercent() {
//        log.info("Starting interest calculation and balance update...");
//
//        List<Long> savingsAccountIds = getAllSavingsAccountIds();
//
//        for (Long savingsAccountId : savingsAccountIds) {
//            executorService.submit(() -> {
//                try {
//                    // Выполнить расчет начисления процентов и обновление баланса для каждого счета
//                    updateBalanceWithInterest(savingsAccountId);
//                    log.info("Interest calculation and balance update completed for account ID: " + savingsAccountId);
//                } catch (Exception e) {
//                    log.error("Error processing account ID: " + savingsAccountId, e);
//                }
//            });
//        }
//
//        log.info("Interest calculation and balance update started for " + savingsAccountIds.size() + " accounts.");
//    }
//
//    @org.springframework.transaction.annotation.Transactional(readOnly = true)
//    public List<Long> getAllSavingsAccountIds() {
//        return savingsAccountRepository.findAllSavingsAccountIds();
//    }
//
//    private void updateBalanceWithInterest(Long savingsAccountId) {
//        try {
//            // Получаем id накопительного счета по id аккаунта
//            Long savingsId = getSavingsAccountIdByAccountId(savingsAccountId);
//
//            // Выполняем расчет процентов и обновляем баланс
//            // Можно реализовать вашу логику расчета процентов здесь
//            // Пример: BigDecimal interest = calculateInterest(savingsId);
//
//            // Обновляем баланс с учетом начисленных процентов
//            // Пример: balanceService.updateBalanceWithInterest(savingsId, interest);
//
//            // В данном месте должен быть ваш расчет процентов и обновление баланса
//        } catch (Exception e) {
//            log.error("Error updating balance with interest for account ID: " + savingsAccountId, e);
//        }
//    }
//
//    //    @Async
////    @Transactional
////    @Retryable(retryFor = {OptimisticLockingFailureException.class})
////    public void calculateAndApplyLastPercents() {
////
////        List<SavingsAccount> savingsAccounts = savingsAccountRepository.findAllByAccountStatus(AccountStatus.OPENED);
////
////        for (SavingsAccount savingsAccount : savingsAccounts) {
////            // Ваш код для расчета процентов и обновления баланса
////            // Можно использовать дату последнего успешного начисления процентов для определения периода расчета
////            // ...
////
////            // Обновление даты последнего успешного начисления процентов
////            savingsAccount.setLastUpdateCalculationAt(LocalDateTime.now());
////        }
////    }
//
//}

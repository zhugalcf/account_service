//package faang.school.accountservice.service;
//
//import faang.school.accountservice.dto.TariffDto;
//import faang.school.accountservice.entity.account.SavingsAccount;
//import faang.school.accountservice.mapper.SavingsAccountMapper;
//import faang.school.accountservice.mapper.TariffMapper;
//import faang.school.accountservice.repository.SavingsAccountRepository;
//import faang.school.accountservice.repository.SavingsAccountTariffHistoryRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.OptimisticLockingFailureException;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PercentCalculationService {
//    private final SavingsAccountRepository savingsAccountRepository;
//    private final SavingsAccountTariffHistoryRepository historyRepository;
//    private final TariffMapper tariffMapper;
//    private final SavingsAccountMapper savingsAccountMapper;
//
//    @Async
//    @Transactional
//    @Retryable(retryFor = {OptimisticLockingFailureException.class})
//    public void percentCalculateAndApply() {
//        List<SavingsAccount> savingsAccounts = savingsAccountRepository.findAll();
//
//        for (SavingsAccount account : savingsAccounts) {
//            List<Long> tariffHistoryIds = account.getTariffHistoryIds();
//
//            TariffDto currentTariff = savingsAccountMapper.mapTariff(tariffHistoryIds, historyRepository, tariffMapper);
//            BigDecimal currentRate = savingsAccountMapper.mapRate(tariffHistoryIds, historyRepository, tariffMapper);
//
//            if (currentTariff != null) {
//                BigDecimal balance = account.getBalance();
//                BigDecimal updateAmount = balance.multiply(currentRate);
//                BigDecimal newBalance = balance.add(updateAmount);
//
//                log.info("Updating balance for account {} from {} to {}", account.getId(), balance, newBalance);
//                account.setBalance(newBalance);
//                account.setLastUpdateCalculationAt(LocalDateTime.now());
//                savingsAccountRepository.save(account);
//                log.info("Updated balance for account {} to {}", account.getId(), newBalance);
//            }
//        }
//    }
//}
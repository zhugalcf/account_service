package faang.school.accountservice.service;

import faang.school.accountservice.config.account.AccountGenerationConfig;
import faang.school.accountservice.entity.account.numbers.AccountNumberSequence;
import faang.school.accountservice.entity.account.numbers.FreeAccountNumber;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.exception.NoFreeAccountNumbersException;
import faang.school.accountservice.repository.AccountNumberSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniqueNumberService {
    private final FreeAccountNumbersRepository freeAccountNumbersRepository;
    private final AccountNumberSequenceRepository accountNumberSequenceRepository;
    private final AccountGenerationConfig accountGenerationConfig;


    @Transactional
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public void generateAccountNumbersOfType(long numberOfAccounts, AccountType accountType) {
        createListAccountNumbers(numberOfAccounts, accountType);
    }

    @Transactional
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public void generateAccountNumbersToReach(long targetCount, AccountType accountType) {

        long currentCount = freeAccountNumbersRepository.countByAccountType(accountType);
        long remainingCount = targetCount - currentCount;

        if (remainingCount > 0) {
            createListAccountNumbers(remainingCount, accountType);
        }
    }

    private void createListAccountNumbers(long remainingCount, AccountType accountType) {
        int accountNumberLength = accountGenerationConfig.getAccountNumberLength();
        List<String> newAccountNumbers = new ArrayList<>();
        for (long i = 0; i < remainingCount; i++) {
            String accountNumber = generateAccountNumber(accountType, accountNumberLength);
            newAccountNumbers.add(accountNumber);
        }

        List<FreeAccountNumber> accountNumbers = newAccountNumbers.stream()
                .map(accountNumber -> FreeAccountNumber.builder()
                        .accountType(accountType)
                        .accountNumber(accountNumber)
                        .build()).collect(Collectors.toList());
        freeAccountNumbersRepository.saveAll(accountNumbers);
    }

    private String generateAccountNumber(AccountType accountType, int length) {
        String prefix = accountType.getFirstNumberOfAccount();
        AccountNumberSequence sequence = getOrCreateSequence(accountType);
        long currentCount = sequence.getCurrentCount();

        sequence.setCurrentCount(++currentCount);
        accountNumberSequenceRepository.save(sequence);
        return prefix + String.format("%0" + (length - prefix.length()) + "d", currentCount);
    }


    private AccountNumberSequence getOrCreateSequence(AccountType accountType) {
        return accountNumberSequenceRepository.findByAccountType(accountType).orElseGet(() -> AccountNumberSequence.builder()
                .accountType(accountType)
                .currentCount(0L)
                .build());
    }

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public String getFreeAccountNumber(AccountType accountType) {
        try {
            Optional<FreeAccountNumber> accountNumber = getFirstFreeAccountNumber(accountType);
            if (accountNumber.isPresent()) {
                FreeAccountNumber freeAccountNumber = accountNumber.get();
                freeAccountNumbersRepository.deleteById(freeAccountNumber.getId());
                return freeAccountNumber.getAccountNumber();
            } else {
                generateAdditionalAccountNumbers(accountType);
                log.warn("No free account numbers. Generated additional account numbers for {}.", accountType);
                accountNumber = getFirstFreeAccountNumber(accountType);
                if (accountNumber.isPresent()) {
                    FreeAccountNumber freeAccountNumber = accountNumber.get();
                    freeAccountNumbersRepository.deleteById(freeAccountNumber.getId());
                    return freeAccountNumber.getAccountNumber();
                }
            }
        } catch (OptimisticLockingFailureException ole) {
            log.warn("Optimistic locking failure while getting free account number: {}", ole.getMessage());
        } catch (Exception e) {
            log.error("Error while getting free account number: {}", e.getMessage());
        }
        throw new NoFreeAccountNumbersException("No free account numbers even after generating additional numbers.");
    }

    private Optional<FreeAccountNumber> getFirstFreeAccountNumber(AccountType accountType) {
        return freeAccountNumbersRepository.findFirstByAccountTypeOrderByCreatedAtAsc(accountType);
    }

    private void generateAdditionalAccountNumbers(AccountType accountType) {
        int additionalNumbersToGenerate = 10;
        createListAccountNumbers(additionalNumbersToGenerate, accountType);
    }
}

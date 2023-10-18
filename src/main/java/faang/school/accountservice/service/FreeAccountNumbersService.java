package faang.school.accountservice.service;

import faang.school.accountservice.config.account.AccountGenerationConfig;
import faang.school.accountservice.entity.account.numbers.FreeAccountNumber;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.exception.NoFreeAccountNumbersException;
import faang.school.accountservice.repository.AccountNumbersSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreeAccountNumbersService {
    private final FreeAccountNumbersRepository freeAccountNumbersRepository;
    private final AccountNumbersSequenceRepository accountNumbersSequenceRepository;
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

        List<String> newAccountNumbers = LongStream.range(0, remainingCount)
                .mapToObj(i -> generateAccountNumber(accountType, accountNumberLength)).toList();

        List<FreeAccountNumber> accountNumbers = newAccountNumbers.stream()
                .map(accountNumber -> FreeAccountNumber.builder()
                        .accountType(accountType)
                        .accountNumber(accountNumber)
                        .build()).collect(Collectors.toList());
        freeAccountNumbersRepository.saveAll(accountNumbers);
    }

    private String generateAccountNumber(AccountType accountType, int length) {
        String prefix = accountType.getFirstNumberOfAccount();

        long currentCount = getOrCreateSequence(accountType);
        accountNumbersSequenceRepository.incrementByAccountType(accountType.ordinal());

        return prefix + String.format("%0" + (length - prefix.length()) + "d", currentCount);
    }

    public Long getOrCreateSequence(AccountType accountType) {
        return accountNumbersSequenceRepository
                .getCurrentCountByAccountType(accountType.ordinal())
                .orElseGet(() -> {
                    accountNumbersSequenceRepository.createAccountNumberSequence(accountType.ordinal());
                    return accountNumbersSequenceRepository
                            .getCurrentCountByAccountType(accountType.ordinal())
                            .orElse(null);
                });
    }

    @Transactional
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public String getFreeAccountNumber(AccountType accountType) {
        try {
            Optional<String> accountNumber = findFirstFreeAccountNumber(accountType);
            if (accountNumber.isPresent()) {
                return accountNumber.get();
            }
        } catch (OptimisticLockingFailureException ole) {
            log.warn("Optimistic locking failure while getting free account number: {}", ole.getMessage());
        } catch (Exception e) {
            log.error("Error while getting free account number: {}", e.getMessage());
        }
        throw new NoFreeAccountNumbersException("No free account numbers even after generating additional numbers.");
    }

    private Optional<String> findFirstFreeAccountNumber(AccountType accountType) {
        Optional<String> accountNumber = freeAccountNumbersRepository.deleteAndReturnFirstByAccountTypeOrderByCreatedAtAsc(accountType.ordinal());
        if (accountNumber.isEmpty()) {
            generateAdditionalAccountNumbers(accountType);
            log.warn("No free account numbers. Generated additional account numbers for {}.", accountType);
            accountNumber = freeAccountNumbersRepository.deleteAndReturnFirstByAccountTypeOrderByCreatedAtAsc(accountType.ordinal());
        }
        return accountNumber;
    }

    private void generateAdditionalAccountNumbers(AccountType accountType) {
        int additionalNumbersToGenerate = 10;
        createListAccountNumbers(additionalNumbersToGenerate, accountType);
    }
}
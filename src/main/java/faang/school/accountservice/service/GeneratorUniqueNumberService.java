package faang.school.accountservice.service;

import faang.school.accountservice.entity.account.numbers.AccountNumberSequence;
import faang.school.accountservice.entity.account.numbers.FreeAccountNumber;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.exception.NoFreeAccountNumbersException;
import faang.school.accountservice.repository.AccountNumberSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneratorUniqueNumberService {

    private final FreeAccountNumbersRepository freeAccountNumbersRepository;
    private final AccountNumberSequenceRepository accountNumberSequenceRepository;


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void generateAccountNumbersOfType(long n, AccountType accountType, int length) {
        List<String> newAccountNumbers = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String accountNumber = generateAccountNumber(accountType, length);
            newAccountNumbers.add(accountNumber);
        }

        List<FreeAccountNumber> accountsNumbers = newAccountNumbers.stream()
                .map(accountNumber -> FreeAccountNumber.builder()
                        .accountType(accountType)
                        .accountNumber(accountNumber)
                        .build())
                .collect(Collectors.toList());

        freeAccountNumbersRepository.saveAll(accountsNumbers);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void generateAccountNumbersToReach(long targetCount, AccountType accountType, Integer length) {

        long currentCount = freeAccountNumbersRepository.countByAccountType(accountType);
        long remainingCount = targetCount - currentCount;

        if (remainingCount > 0) {
            List<String> newAccountNumbers = new ArrayList<>();
            for (int i = 0; i < remainingCount; i++) {
                String accountNumber = generateAccountNumber(accountType, length);
                newAccountNumbers.add(accountNumber);
            }

            List<FreeAccountNumber> accountNumbers = newAccountNumbers.stream()
                    .map(accountNumber -> FreeAccountNumber.builder()
                            .accountType(accountType)
                            .accountNumber(accountNumber)
                            .build()).collect(Collectors.toList());
            freeAccountNumbersRepository.saveAll(accountNumbers);
        }
    }

    @Transactional
    public String generateAccountNumber(AccountType accountType, int length) {
        String prefix = accountType.getGetNumberAccountType();
        AccountNumberSequence sequence = getOrCreateSequence(accountType);
        long currentCount = sequence.getCurrentCount();

        sequence.setCurrentCount(currentCount + 1);
        accountNumberSequenceRepository.save(sequence);
        StringBuilder accountNumber = new StringBuilder(prefix);
        accountNumber.append(String.format("%0" + (length - prefix.length()) + "d", currentCount));
        return accountNumber.toString();
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public AccountNumberSequence getOrCreateSequence(AccountType accountType) {
        AccountNumberSequence sequence = accountNumberSequenceRepository.findByAccountType(accountType);
        if (sequence == null) {
            sequence = AccountNumberSequence.builder()
                    .accountType(accountType)
                    .currentCount(1L)
                    .build();
            accountNumberSequenceRepository.save(sequence);
        }
        return sequence;
    }

    public String getFreeAccountNumber(AccountType accountType) {
        Optional<FreeAccountNumber> accountNumber = freeAccountNumbersRepository.findFirstByAccountTypeOrderByCreatedAtAsc(accountType);
        if (accountNumber.isPresent()) {
            FreeAccountNumber freeAccountNumber = accountNumber.get();
            freeAccountNumbersRepository.deleteById(freeAccountNumber.getId());
            return freeAccountNumber.getAccountNumber();
        }

        throw new NoFreeAccountNumbersException("No free account numbers");
    }
}

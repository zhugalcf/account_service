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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniqueNumberService {

    private final FreeAccountNumbersRepository freeAccountNumbersRepository;
    private final AccountNumberSequenceRepository accountNumberSequenceRepository;


    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void generateAccountNumbersOfType(long numberOfAccounts, AccountType accountType, int length) {
        createListAccountNumbers(numberOfAccounts, accountType, length);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void generateAccountNumbersToReach(long targetCount, AccountType accountType, Integer length) {

        long currentCount = freeAccountNumbersRepository.countByAccountType(accountType);
        long remainingCount = targetCount - currentCount;

        if (remainingCount > 0) {
            createListAccountNumbers(remainingCount, accountType, length);
        }
    }

    private void createListAccountNumbers(long remainingCount, AccountType accountType, Integer length) {
        List<String> newAccountNumbers = new ArrayList<>();
        for (long i = 0; i < remainingCount; i++) {
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

    public String generateAccountNumber(AccountType accountType, int length) {
        String prefix = accountType.getFirstNumberOfAccount();
        AccountNumberSequence sequence = getOrCreateSequence(accountType);
        long currentCount = sequence.getCurrentCount();

        sequence.setCurrentCount(currentCount + 1);
        accountNumberSequenceRepository.save(sequence);
        return prefix + String.format("%0" + (length - prefix.length()) + "d", currentCount);
    }


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

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
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

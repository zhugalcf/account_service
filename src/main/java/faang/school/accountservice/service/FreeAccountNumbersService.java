package faang.school.accountservice.service;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.model.FreeAccountNumber;
import faang.school.accountservice.repository.AccountNumberSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreeAccountNumbersService {
    private final FreeAccountNumberRepository freeAccountNumberRepository;
    private final AccountNumberSequenceRepository accountNumberSequenceRepository;
    private final ReentrantLock lock = new ReentrantLock();

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 3000))
    public void generateAccountNumber(int amountNum, AccountType accountType) {
        if (lock.tryLock()) {
            try {
                var sequence = accountNumberSequenceRepository.findByAccountType(accountType).orElseThrow(
                        () -> new NotFoundException("Account type is not found")
                );
                Long currentCount = accountNumberSequenceRepository.incrementCurrentCount(sequence.getAccountType().ordinal(), sequence.getCurrentCount())
                        .orElseThrow(
                                () -> {
                                    String errorMessage = "Failed to increment count for:" + accountType;
                                    log.error(errorMessage);
                                    return new RuntimeException(errorMessage);
                                }
                        );

                List<FreeAccountNumber> freeAccountNumbers = new ArrayList<>();
                for (int i = 0; i < amountNum; i++) {
                    String accountNumber = String.format("%s%08d", accountType.getIdentityString(), currentCount);
                    FreeAccountNumber freeAccountNumber = FreeAccountNumber.builder()
                            .accountType(accountType)
                            .accountNumber(accountNumber)
                            .build();

                    freeAccountNumbers.add(freeAccountNumber);
                    currentCount++;
                }

                freeAccountNumberRepository.saveAll(freeAccountNumbers);

                accountNumberSequenceRepository.incrementCurrentCount(sequence.getAccountType().ordinal(), currentCount);
            } finally {
                lock.unlock();
            }
        } else {
            log.error("Failed to acquire the lock for generating account numbers.");
        }
    }

    @Transactional
    public void perform(int amountNum, AccountType accountType, Consumer<String> action) {
        int countNum = freeAccountNumberRepository.getCountForAccountType(accountType.ordinal());
        if (countNum < amountNum) {
            int toGenerate = amountNum - countNum;
            generateAccountNumber(toGenerate, accountType);
        }

        var freeNumber = freeAccountNumberRepository.getAccountNumber(accountType.ordinal())
                .orElseThrow(() -> {
                    throw new NotFoundException("Free number is not found for " + accountType);
                });
        action.accept(freeNumber.toString());
    }
}

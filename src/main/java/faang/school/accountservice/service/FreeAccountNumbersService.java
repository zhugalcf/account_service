package faang.school.accountservice.service;

import faang.school.accountservice.model.AccountType;
import faang.school.accountservice.model.FreeAccountNumber;
import faang.school.accountservice.repository.AccountNumberSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumberRepository;
import faang.school.accountservice.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreeAccountNumbersService {
    private final FreeAccountNumberRepository freeAccountNumberRepository;
    private final AccountNumberSequenceRepository accountNumberSequenceRepository;

    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 3000)) // Не рабоатет
    public void generateAccountNumber(AccountType accountType) {
        var sequence = accountNumberSequenceRepository.findByAccountType(accountType).orElseThrow(
                () -> new NotFoundException("No such account type found")
        );
        Long currentCount = accountNumberSequenceRepository.incrementCurrentCount(sequence.getAccountType().ordinal(), sequence.getCurrentCount())
                .orElseThrow(
                        () -> {
                            String errorMessage = "Failed to increment count for:" + accountType;
                            log.error(errorMessage);
                            return new RuntimeException(errorMessage);
                        }
                );
        String accountNumber = String.format("%s%08d", accountType.getAssociatedString(), currentCount);
        FreeAccountNumber freeAccountNumber = FreeAccountNumber.builder()
                .accountType(accountType)
                .accountNumber(accountNumber)
                .build();
        freeAccountNumberRepository.save(freeAccountNumber);
    }

    @Transactional
    public void perform(AccountType accountType, Consumer<String> action) {
        var freeNumber = freeAccountNumberRepository.getAccountNumber(accountType.ordinal())
                .orElseGet(() -> {
                    generateAccountNumber(accountType);
                    throw new NotFoundException("No free number for " + accountType);
                });
        action.accept(freeNumber.toString());
    }
}

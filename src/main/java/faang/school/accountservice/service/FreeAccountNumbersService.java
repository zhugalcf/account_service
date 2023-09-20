package faang.school.accountservice.service;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.AccountNumber;
import faang.school.accountservice.model.AccountNumbersSequence;
import faang.school.accountservice.repository.AccountNumbersSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreeAccountNumbersService {

    private final Environment environment;
    private final FreeAccountNumbersRepository numbersRepository;
    private final AccountNumbersSequenceRepository sequenceRepository;
    @Value("${account_numbers.number_length}")
    private int numberLength;

    public AccountNumber createNumber(AccountType type) {
        return null;
    }

    public long getAndIncrementSequence(AccountType type) {
        long result = sequenceRepository.getAccountNumbersSequenceByType(type).getCurrent();
        boolean optimisticLock = false;
        while (!optimisticLock) {
            optimisticLock = sequenceRepository.increment(type);
        }
        return result;
    }
}

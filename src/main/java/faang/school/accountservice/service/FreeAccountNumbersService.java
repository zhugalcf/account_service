package faang.school.accountservice.service;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.exception.NumberCreationException;
import faang.school.accountservice.exception.SequenceOverflowException;
import faang.school.accountservice.model.account.number.AccountNumber;
import faang.school.accountservice.repository.AccountNumbersSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreeAccountNumbersService {

    private final Environment environment;
    private final FreeAccountNumbersRepository numbersRepository;
    private final AccountNumbersSequenceRepository sequenceRepository;
    @Value("${account-numbers.number_length}")
    private int numberLength;
    @Value("${account-numbers.type_code_length}")
    private int typeCodeLength;
    @Value("${account-numbers.account_numbers_buffer_size}")
    private int freeNumbersBufferSize;
    @Value("${account-numbers.max_retry_count}")
    private int MAX_RETRY_COUNT;

    @Transactional
    public String getNumber(AccountType type, BiConsumer<AccountType, String> biConsumer) {
        log.info("Method getNumber was called with type {}.", type);

        AccountNumber accountNumber = numbersRepository.findFreeNumber(type.toString());
        biConsumer.accept(type, accountNumber.getAccount_number());
        numbersRepository.delete(accountNumber);

        return accountNumber.getAccount_number();
    }

    @Transactional
    public long getAndIncrementSequence(AccountType type) {
        log.info("Method getAndIncrementSequence was called with type {}.", type);

        long current = sequenceRepository.findByType(type.toString());
        int result = sequenceRepository.increment(type.toString(), current);
        int count = 0;

        while (result < 0) {
            result = sequenceRepository.increment(type.toString(), current);
            count++;
            if (count >= MAX_RETRY_COUNT) {
                throw new NumberCreationException(String
                        .format("The number of attempts to increment the sequence with type %s has been exceeded.", type.toString()));
            }
        }

        return ++current;
    }

    private String generateNumber(AccountType type) {
        log.info("Method generateNumber was called with type {}.", type);

        String typeCode = environment.getProperty("account-numbers.type_codes." + type.toString());
        long sequence = getAndIncrementSequence(type);
        checkSequenceOverflow(sequence, type);
        String accountNumber = typeCode + String.format("%0" + (numberLength - typeCodeLength) + "d", sequence);
        return accountNumber;
    }

    public void generateNumbers(AccountType type, long numbersCount, int bufferSize) {
        log.info("Method generateNumbers was called with type {}.", type);

        for (long i = numbersCount; i <= bufferSize; i++) {
            String number = generateNumber(type);
            numbersRepository.createNewNumber(type.toString(), number);
        }
    }

    @Scheduled(fixedDelayString = "${account-numbers.generation_delay}")
    @Async("numberGenerator")
    protected void generate() {
        System.out.println(Arrays.toString(AccountType.values()));
        Arrays.stream(AccountType.values())
                .forEach((type) -> {
                    checkFreeNumbersCountAndGenerateNumbers(type);
                });
    }

    private void checkFreeNumbersCountAndGenerateNumbers(AccountType type) {
        log.info("Method checkFreeNumbersCountAndGenerateNumbers was called with type {}.", type);

        sequenceRepository.createNewCounterIfNotExists(type.toString());
        long numbersCount = numbersRepository.countAccountNumberByType(type.toString());
        if (numbersCount <= freeNumbersBufferSize / 2) {
            generateNumbers(type, numbersCount, freeNumbersBufferSize);
        }
    }

    private void checkSequenceOverflow(long sequence, AccountType type) {
        log.info("Method checkSequenceOverflow was called with current {}.", sequence);

        if (String.valueOf(sequence).length() > (numberLength - typeCodeLength)) {
            log.error("Sequence with type {} is overflow.", type);
            throw new SequenceOverflowException("Sequence is overflow.");
        }
    }
}

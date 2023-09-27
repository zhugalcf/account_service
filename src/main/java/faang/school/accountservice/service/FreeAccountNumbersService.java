package faang.school.accountservice.service;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.exception.NumberCreationException;
import faang.school.accountservice.exception.SequenceOverflowException;
import faang.school.accountservice.model.AccountNumber;
import faang.school.accountservice.repository.AccountNumbersSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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

    @Transactional
    public String getNumber(AccountType type, BiConsumer<AccountType, String> biConsumer) {
        String number = getNumber(type);
        biConsumer.accept(type, number);
        return number;
    }

    @Transactional
    public String getNumber(AccountType type) {
        log.info("Method getNumber was called with type {}.", type);

        sequenceRepository.createNewCounterIfNotExists(type.toString());
        checkFreeNumbersCountAndGenerateNumbers(type);

        AccountNumber accountNumber = numbersRepository.findFreeNumber(type.toString());
        numbersRepository.deleteFreeNumber(accountNumber.getId());
        checkNumberIsDelete(accountNumber);

        return accountNumber.getAccount_number();
    }

    @Transactional
    public long getAndIncrementSequence(AccountType type) {
        log.info("Method getAndIncrementSequence was called with type {}.", type);
        long current = sequenceRepository.findByType(type.toString());
        int result = sequenceRepository.increment(type.toString(), current);
        if (result < 0) {
            current = getAndIncrementSequence(type);
        } else {
            current = sequenceRepository.findByType(type.toString());
        }
        return current;
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

    private void checkFreeNumbersCountAndGenerateNumbers(AccountType type) {
        log.info("Method checkFreeNumbersCountAndGenerateNumbers was called with type {}.", type);
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

    private void checkNumberIsDelete(AccountNumber accountNumber) {
        log.info("Method checkNumberIsDelete was called with number {}.", accountNumber.getAccount_number());
        if (numbersRepository.existsById(accountNumber.getId())) {
            log.error("The number {} was not deleted", accountNumber.getAccount_number());
            throw new NumberCreationException("The number was not deleted");
        }
    }
}

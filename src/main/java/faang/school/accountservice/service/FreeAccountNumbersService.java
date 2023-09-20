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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreeAccountNumbersService {

    private final FreeAccountNumbersRepository freeAccountNumbersRepository;
    private final AccountNumbersSequenceRepository accountNumbersSequenceRepository;
    @Value("${account_numbers.number_length}")
    private int numberLength;

//    public AccountNumber createNewNumber(AccountType type){
//        AccountNumbersSequence current = null;
//        try {
//            current = accountNumbersSequenceRepository.getById(type);
//        }catch (EntityNotFoundException exception){
//            accountNumbersSequenceRepository.createNewCounter(type);
//            current = accountNumbersSequenceRepository.getById(type);
//        }
//
//        String number = String.format("%0" + numberLength + "d", current);
//        number = String.format("${account_numbers.%s}", type) + number;
//
//        log.info("Account number {} was created", number);
//
//        return freeAccountNumbersRepository.save(new AccountNumber(number, type));
//    }

    public void createNewSequence(AccountType type){
//        accountNumbersSequenceRepository.createNewCounter(type);
        AccountNumbersSequence sequence = new AccountNumbersSequence(1, type, 0, 0);
        accountNumbersSequenceRepository.save(sequence);
    }
}

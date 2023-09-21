package faang.school.accountservice.service;

import faang.school.accountservice.entity.FreeAccountNumber;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.repository.AccountNumbersSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class FreeAccountNumbersService {

    private final FreeAccountNumberRepository freeAccountNumberRepository;
    private final AccountNumbersSequenceRepository accountNumbersSequenceRepository;

    @Transactional
    public FreeAccountNumber createNewFreeAccountNumber(FreeAccountNumber freeAccountNumber) {
        return freeAccountNumberRepository.save(freeAccountNumber);
    }

    @Transactional
    public String findAndDeleteFirstFreeAccountNumber(AccountType accountType) {
        FreeAccountNumber freeAccountNumber = freeAccountNumberRepository.findFirstByOrderByAccountNumber(accountType);

        if (freeAccountNumber != null) {
            String accountNumber = freeAccountNumber.getAccountNumber();
            freeAccountNumberRepository.deleteFirstByAccountNumber(accountNumber);
            return accountNumber;
        }
        return null;
    }

    @Transactional
    public void executeInTransactionWithFreeAccountNumber(Consumer<String> transactionalMethod) {
//        Optional<String> freeAccountNumber = freeAccountNumberRepository.findFirstByOrderByAccountNumber();
//
//        if (freeAccountNumber.isPresent()) {
//            String accountNumber = freeAccountNumber.get();
//            freeAccountNumberRepository.deleteFirstByAccountNumber(accountNumber);
//            transactionalMethod.accept(accountNumber);
//        } else {
//            // Если свободного номера счета нет, генерируем новый
//            String newAccountNumber = generateNewAccountNumber();
//            transactionalMethod.accept(newAccountNumber);
//        }
    }

    private String generateNewAccountNumber() {
        // Здесь реализуйте логику для генерации нового номера счета
        // Например, инкрементируйте счетчик и верните новый номер
        // Ваш код может выглядеть примерно так:
//        Long currentMaxAccountNumber = accountNumbersSequenceRepository.findMaxAccountNumber();
//        Long newAccountNumber = currentMaxAccountNumber != null ? currentMaxAccountNumber + 1 : 1;
//        return newAccountNumber.toString();
        return null;
    }
}
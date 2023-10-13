package faang.school.accountservice.service;

import faang.school.accountservice.entity.AccountNumberSequence;
import faang.school.accountservice.entity.FreeAccountId;
import faang.school.accountservice.entity.FreeAccountNumber;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.repository.AccountSequenceRepository;
import faang.school.accountservice.repository.FreeAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class FreeAccountNumberService {

    private static final long ACCOUNT_PATTERN = 4200_0000_0000_0000L;

    private final AccountSequenceRepository accountSeqRepository;
    private final FreeAccountRepository freeAccountRepository;


    @Transactional
    public void generateAccountNumbers(AccountType type, int batchSize) {
        AccountNumberSequence period =  accountSeqRepository.incrementCounter(type.name(), batchSize);
        List<FreeAccountNumber> numbers = new ArrayList<>();
        for(long i = period.getInitialValue(); i< period.getCounter(); i++) {
            numbers.add(new FreeAccountNumber(new FreeAccountId(type, ACCOUNT_PATTERN+i)));
        }
        freeAccountRepository.saveAll(numbers);
    }

    @Transactional
    public void retrieveAccountNumbers(AccountType type, Consumer<FreeAccountNumber> numberConsumer) {
        numberConsumer.accept(freeAccountRepository.retrieveFirst(type.name()));
    }
}

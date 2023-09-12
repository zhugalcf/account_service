package faang.school.accountservice.service;

import faang.school.accountservice.model.AccountNumber;
import faang.school.accountservice.model.AccountNumberType;
import faang.school.accountservice.model.AccountNumbersSequence;
import faang.school.accountservice.repository.AccountNumbersSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FreeAccountNumbersService {

    private final FreeAccountNumbersRepository freeAccountNumbersRepository;
    private final AccountNumbersSequenceRepository accountNumbersSequenceRepository;
    private final Set<AccountNumberType> accountNumberTypeSet;

    @PostConstruct
    private void init(){
        accountNumberTypeSet.stream().forEach(type -> accountNumbersSequenceRepository.createNewCounter(type));
    }

    public AccountNumber createNewNumber(AccountNumberType type){
        AccountNumbersSequence current = accountNumbersSequenceRepository.getReferenceById(type);
        return null;
    }
    
}

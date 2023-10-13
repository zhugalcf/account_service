package faang.school.accountservice.service;

import faang.school.accountservice.entity.AccountNumberSequence;
import faang.school.accountservice.entity.FreeAccountId;
import faang.school.accountservice.entity.FreeAccountNumber;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.repository.AccountSequenceRepository;
import faang.school.accountservice.repository.FreeAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreeAccountNumberServiceTest {
    @InjectMocks
    private FreeAccountNumberService freeAccountNumberService;
    @Mock
    private AccountSequenceRepository accountSequenceRepository;
    @Mock
    private FreeAccountRepository freeAccountRepository;

    @Test
    void testGenerateAccountNumbers() {
        AccountType accountType = AccountType.CREDIT;
        int batchSize = 100;
        AccountNumberSequence accountNumberSequence = AccountNumberSequence.builder()
                .type(accountType)
                .counter(100)
                .build();

        when(accountSequenceRepository.incrementCounter(accountType.name(), batchSize))
                .thenReturn(accountNumberSequence);

        freeAccountNumberService.generateAccountNumbers(accountType, batchSize);

        verify(accountSequenceRepository, times(1))
                .incrementCounter(accountType.name(), batchSize);
        verify(freeAccountRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testRetrieveAccountNumbers() {
        AccountType accountType = AccountType.DEBIT;
        Consumer<FreeAccountNumber> consumer = Mockito.mock(Consumer.class);

        when(freeAccountRepository.retrieveFirst(accountType.name()))
                .thenReturn(new FreeAccountNumber(new FreeAccountId(accountType, 4200_0000_0000_0000L)));

        freeAccountNumberService.retrieveAccountNumbers(accountType, consumer);

        verify(freeAccountRepository, times(2)).retrieveFirst(accountType.name());
        verify(consumer, times(1)).accept(Mockito.any(FreeAccountNumber.class));
    }
}

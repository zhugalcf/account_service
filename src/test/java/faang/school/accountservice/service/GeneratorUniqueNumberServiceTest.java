package faang.school.accountservice.service;

import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.numbers.AccountNumberSequence;
import faang.school.accountservice.entity.account.numbers.FreeAccountNumber;
import faang.school.accountservice.exception.NoFreeAccountNumbersException;
import faang.school.accountservice.repository.AccountNumberSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneratorUniqueNumberServiceTest {
    @InjectMocks
    private GeneratorUniqueNumberService generatorUniqueNumberService;
    @Mock
    private FreeAccountNumbersRepository freeAccountNumbersRepository;
    @Mock
    private AccountNumberSequenceRepository accountNumberSequenceRepository;
    private AccountType accountType;
    private int length;
    private long targetCountAccounts;
    private FreeAccountNumber freeAccountNumber;
    private AccountNumberSequence accountNumberSequence;

    @BeforeEach
    void setUp() {
        accountType = AccountType.SAVINGS_ACCOUNT;
        length = 10;
        targetCountAccounts = 10L;
        accountNumberSequence = AccountNumberSequence.builder()
                .currentCount(2L)
                .accountType(accountType)
                .build();

        freeAccountNumber = FreeAccountNumber.builder()
                .accountType(accountType)
                .accountNumber("43240000000000000001")
                .build();
    }

    @Test
    void generateAccountNumbersOfType_shouldSaveAllFreeAccountNumbers() {
        when(freeAccountNumbersRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        generatorUniqueNumberService.generateAccountNumbersOfType(targetCountAccounts, accountType, length);

        verify(freeAccountNumbersRepository).saveAll(argThat(argument -> argument.spliterator().estimateSize() == targetCountAccounts));
    }

    @Test
    void generateAccountNumbersToReach_shouldSaveAllFreeAccountNumbers() {
        when(freeAccountNumbersRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        generatorUniqueNumberService.generateAccountNumbersToReach(targetCountAccounts, accountType, length);

        verify(freeAccountNumbersRepository).saveAll(argThat(argument -> argument.spliterator().estimateSize() == targetCountAccounts));
    }

    @Test
    void generateAccountNumber_shouldReturnGeneratedAccountNumber() {
        when(accountNumberSequenceRepository.findByAccountType(accountType)).thenReturn(accountNumberSequence);
        when(accountNumberSequenceRepository.save(accountNumberSequence)).thenReturn(accountNumberSequence);
        String accountNumber  = "4255000002";

        String generatedAccountNumber = generatorUniqueNumberService.generateAccountNumber(accountType, length);

        assertTrue(generatedAccountNumber.startsWith(accountType.getGetNumberAccountType()));

        assertEquals(length, generatedAccountNumber.length());
        assertEquals(accountNumber, generatedAccountNumber);

        verify(accountNumberSequenceRepository, times(1)).save(accountNumberSequence);
    }

    @Test
    void getOrCreateSequence_shouldReturnExistingSequence() {
        when(accountNumberSequenceRepository.findByAccountType(accountType)).thenReturn(accountNumberSequence);
        AccountNumberSequence result = generatorUniqueNumberService.getOrCreateSequence(accountType);

        assertEquals(accountNumberSequence, result);

        verify(accountNumberSequenceRepository, never()).save(any(AccountNumberSequence.class));
    }

    @Test
    void getOrCreateSequence_shouldCreateNewSequence() {
        when(accountNumberSequenceRepository.findByAccountType(accountType)).thenReturn(null);

        AccountNumberSequence result = generatorUniqueNumberService.getOrCreateSequence(accountType);

        assertNotNull(result);
        assertEquals(accountType, result.getAccountType());
        assertEquals(1L, result.getCurrentCount());

        verify(accountNumberSequenceRepository).save(any(AccountNumberSequence.class));
    }

    @Test
    void getFreeAccountNumber() {
        when(freeAccountNumbersRepository.findFirstByAccountTypeOrderByCreatedAtAsc(accountType))
                .thenReturn(Optional.empty());

        assertThrows(NoFreeAccountNumbersException.class, () -> generatorUniqueNumberService.getFreeAccountNumber(accountType));
    }

    @Test
    void testGetFreeAccountNumberWhenNumbersAvailable() {
        when(freeAccountNumbersRepository.findFirstByAccountTypeOrderByCreatedAtAsc(accountType))
                .thenReturn(Optional.of(freeAccountNumber));

        String accountNumber = generatorUniqueNumberService.getFreeAccountNumber(accountType);

        assertEquals(freeAccountNumber.getAccountNumber(), accountNumber);

        verify(freeAccountNumbersRepository).deleteById(freeAccountNumber.getId());
    }

    @Test
    void testGetFreeAccountNumberWhenNoNumbersAvailable() {
        when(freeAccountNumbersRepository.findFirstByAccountTypeOrderByCreatedAtAsc(accountType))
                .thenReturn(Optional.empty());

        assertThrows(NoFreeAccountNumbersException.class, () -> generatorUniqueNumberService.getFreeAccountNumber(accountType));
    }
}
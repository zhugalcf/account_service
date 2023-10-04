package faang.school.accountservice.service;

import faang.school.accountservice.config.account.AccountGenerationConfig;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.numbers.FreeAccountNumber;
import faang.school.accountservice.exception.NoFreeAccountNumbersException;
import faang.school.accountservice.repository.AccountNumbersSequenceRepository;
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
class FreeAccountNumbersServiceTest {
    @InjectMocks
    private FreeAccountNumbersService freeAccountNumbersService;
    @Mock
    private FreeAccountNumbersRepository freeAccountNumbersRepository;
    @Mock
    private AccountNumbersSequenceRepository accountNumbersSequenceRepository;
    @Mock
    private AccountGenerationConfig accountGenerationConfig;
    private AccountType accountType;
    private long targetCountAccounts;
    private FreeAccountNumber freeAccountNumber;
    private String accountNumber;

    @BeforeEach
    void setUp() {
        accountType = AccountType.SAVINGS_ACCOUNT;
        targetCountAccounts = 10L;

        freeAccountNumber = FreeAccountNumber.builder()
                .accountType(accountType)
                .accountNumber("52360000000000000001")
                .build();
        accountNumber = "52360000000000000001";
    }

    @Test
    void generateAccountNumbersOfType_shouldSaveAllFreeAccountNumbers() {
        when(accountGenerationConfig.getAccountNumberLength()).thenReturn(20);
        when(freeAccountNumbersRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        when(accountNumbersSequenceRepository.getCurrentCountByAccountType(anyInt())).thenReturn(Optional.of(1L));

        freeAccountNumbersService.generateAccountNumbersOfType(targetCountAccounts, accountType);

        verify(freeAccountNumbersRepository).saveAll(argThat(argument -> argument.spliterator().estimateSize() == targetCountAccounts));
    }

    @Test
    void generateAccountNumbersToReach_shouldSaveAllFreeAccountNumbers() {
        when(accountGenerationConfig.getAccountNumberLength()).thenReturn(20);
        when(freeAccountNumbersRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        when(accountNumbersSequenceRepository.getCurrentCountByAccountType(anyInt())).thenReturn(Optional.of(1L));

        freeAccountNumbersService.generateAccountNumbersToReach(targetCountAccounts, accountType);

        verify(freeAccountNumbersRepository).saveAll(argThat(argument -> argument.spliterator().estimateSize() == targetCountAccounts));
    }

    @Test
    void getFreeAccountNumber() {
        when(freeAccountNumbersRepository.deleteAndReturnFirstByAccountTypeOrderByCreatedAtAsc(accountType.ordinal()))
                .thenReturn(Optional.empty());

        assertThrows(NoFreeAccountNumbersException.class, () -> freeAccountNumbersService.getFreeAccountNumber(accountType));
    }

    @Test
    void testGetFreeAccountNumberWhenNumbersAvailable() {
        when(freeAccountNumbersRepository.deleteAndReturnFirstByAccountTypeOrderByCreatedAtAsc(accountType.ordinal()))
                .thenReturn(Optional.of(accountNumber));

        String accountNumber = freeAccountNumbersService.getFreeAccountNumber(accountType);

        assertEquals(freeAccountNumber.getAccountNumber(), accountNumber);

        verify(freeAccountNumbersRepository).deleteAndReturnFirstByAccountTypeOrderByCreatedAtAsc(accountType.ordinal());
    }

    @Test
    void testGetFreeAccountNumberWhenNoNumbersAvailable() {
        when(freeAccountNumbersRepository.deleteAndReturnFirstByAccountTypeOrderByCreatedAtAsc(accountType.ordinal()))
                .thenReturn(Optional.empty());

        assertThrows(NoFreeAccountNumbersException.class, () -> freeAccountNumbersService.getFreeAccountNumber(accountType));
    }
}
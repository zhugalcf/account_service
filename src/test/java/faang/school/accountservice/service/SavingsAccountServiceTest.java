package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountCreateDto;
import faang.school.accountservice.dto.SavingsAccountResponseDto;
import faang.school.accountservice.dto.SavingsAccountUpdateDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.exception.TariffAlreadyAssignedException;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.mapper.SavingsAccountMapperImpl;
import faang.school.accountservice.mapper.SavingsAccountResponseMapper;
import faang.school.accountservice.mapper.SavingsAccountResponseMapperImpl;
import faang.school.accountservice.mapper.TariffMapper;
import faang.school.accountservice.mapper.TariffMapperImpl;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.repository.SavingsAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavingsAccountServiceTest {

    @Mock
    private SavingsAccountRepository repository;
    @Mock
    private AccountService accountService;
    @Mock
    private TariffService tariffService;
    @Spy
    private TariffMapper tariffMapper = new TariffMapperImpl();
    @Spy
    private SavingsAccountMapper savingsAccountMapper = new SavingsAccountMapperImpl();
    @Spy
    private SavingsAccountResponseMapper responseMapper = new SavingsAccountResponseMapperImpl(tariffMapper);
    @Mock
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @InjectMocks
    private SavingsAccountService savingsAccountService;

    private SavingsAccount savingsAccountWithoutHistory;
    private SavingsAccount savingsAccountWithHistory;
    private SavingsAccount savingsAccountWithBalance;

    private SavingsAccountCreateDto createDto;
    private SavingsAccountUpdateDto updateDto;
    private SavingsAccountResponseDto responseDto;

    private Account account;

    private Tariff basicTariff;
    private Tariff promoTariff;

    private TariffDto basicTariffDto;
    private TariffDto promoTariffDto;

    private TariffHistory historyWithBasicTariff;
    private TariffHistory historyWithPromoTariff;

    private Rate basicTariffRate;
    private Rate promoTariffRate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private final BigDecimal moneyAmount = BigDecimal.valueOf(137.3);
    private final String accountNumber = "4159000000000001";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(savingsAccountService, "batchSize", 10);
        createdAt = LocalDateTime.now().minusMonths(1);
        updatedAt = LocalDateTime.now().minusDays(1);
        basicTariffRate = Rate.builder()
                .percent(2.5f)
                .build();
        promoTariffRate = Rate.builder()
                .percent(5.5f)
                .build();
        basicTariff = Tariff.builder()
                .id(5)
                .type(TariffType.BASIC)
                .rateHistory(new ArrayList<>(List.of(basicTariffRate)))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        promoTariff = Tariff.builder()
                .id(6)
                .type(TariffType.PROMO)
                .rateHistory(new ArrayList<>(List.of(promoTariffRate)))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        basicTariffDto = TariffDto.builder()
                .id(5)
                .type(TariffType.BASIC)
                .ratePercent(2.5f)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        promoTariffDto = TariffDto.builder()
                .id(6)
                .type(TariffType.PROMO)
                .ratePercent(5.5f)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        historyWithBasicTariff = TariffHistory.builder()
                .tariff(basicTariff)
                .build();
        historyWithPromoTariff = TariffHistory.builder()
                .tariff(promoTariff)
                .build();
        createDto = SavingsAccountCreateDto.builder()
                .accountId(2)
                .tariffType(TariffType.BASIC)
                .build();
        updateDto = SavingsAccountUpdateDto.builder()
                .savingsAccountId(1)
                .tariffType(TariffType.PROMO)
                .build();
        responseDto = SavingsAccountResponseDto.builder()
                .accountId(2)
                .version(1)
                .tariffDto(basicTariffDto)
                .build();
        account = Account.builder()
                .id(2)
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.OPEN)
                .build();
        savingsAccountWithoutHistory = SavingsAccount.builder()
                .account(account)
                .version(1)
                .build();
        savingsAccountWithHistory = SavingsAccount.builder()
                .account(account)
                .version(1)
                .tariffHistory(new ArrayList<>(List.of(historyWithBasicTariff)))
                .build();
        savingsAccountWithBalance = SavingsAccount.builder()
                .id(1)
                .accountNumber(accountNumber)
                .account(account)
                .balance(BigDecimal.valueOf(62.7))
                .version(1)
                .tariffHistory(new ArrayList<>(List.of(historyWithBasicTariff)))
                .build();
    }

    @Test
    void openSavingsAccountTest() {
        when(accountService.getAccountById(2)).thenReturn(account);
        when(tariffService.assignTariffToSavingsAccount(savingsAccountWithoutHistory, TariffType.BASIC)).thenReturn(historyWithBasicTariff);

        SavingsAccountResponseDto result = savingsAccountService.openSavingsAccount(createDto);

        assertEquals(responseDto, result);

        verify(accountService).getAccountById(2);
        verify(repository).save(savingsAccountWithHistory);
        verify(tariffService).assignTariffToSavingsAccount(savingsAccountWithHistory, TariffType.BASIC);
    }

    @Test
    void changeSavingsAccountTariffFirstScenarioTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(savingsAccountWithHistory));
        when(tariffService.assignTariffToSavingsAccount(savingsAccountWithHistory, TariffType.PROMO)).thenReturn(historyWithPromoTariff);

        SavingsAccountResponseDto result = savingsAccountService.changeSavingsAccountTariff(updateDto);

        assertEquals(2, result.getAccountId());
        assertEquals(promoTariffDto, result.getTariffDto());
        assertEquals(2, result.getVersion());

        verify(repository).findById(1L);
        verify(tariffService).assignTariffToSavingsAccount(savingsAccountWithHistory, TariffType.PROMO);
    }

    @Test
    void changeSavingsAccountTariffSecondScenarioTest() {
        SavingsAccount savingsAccount = SavingsAccount.builder()
                .tariffHistory(new ArrayList<>(List.of(historyWithPromoTariff)))
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(savingsAccount));

        TariffAlreadyAssignedException exception = assertThrows(TariffAlreadyAssignedException.class,
                () -> savingsAccountService.changeSavingsAccountTariff(updateDto));

        assertEquals("Tariff: PROMO, already assigned to saving account with id: 1", exception.getMessage());
    }

    @Test
    void getSavingsAccountByTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(savingsAccountWithHistory));

        SavingsAccountResponseDto result = savingsAccountService.getSavingsAccountDtoBy(1);

        assertEquals(responseDto, result);

        verify(repository).findById(1L);
    }

    @Test
    void findSavingsAccountByTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(savingsAccountWithHistory));

        SavingsAccount result = savingsAccountService.getSavingsAccountBy(1);

        assertEquals(savingsAccountWithHistory, result);
    }

    @Test
    void addFundsToSavingsAccountTest() {
        SavingsAccountUpdateDto savingsAccountUpdateDto = SavingsAccountUpdateDto.builder()
                .savingsAccountId(1)
                .moneyAmount(moneyAmount)
                .build();
        SavingsAccountResponseDto expected = SavingsAccountResponseDto.builder()
                .id(1)
                .accountNumber(accountNumber)
                .accountId(2)
                .balance(BigDecimal.valueOf(200.0))
                .version(2)
                .tariffDto(basicTariffDto)
                .build();


        when(repository.findById(1L)).thenReturn(Optional.of(savingsAccountWithBalance));


        SavingsAccountResponseDto result = savingsAccountService.addFundsToSavingsAccount(savingsAccountUpdateDto);

        assertEquals(expected, result);

        verify(repository).findById(1L);
    }

    @Test
    void calculateSavingsAccountRatePercentTest() {
        List<SavingsAccount> accounts = new ArrayList<>(List.of(savingsAccountWithBalance));
        when(repository.findAll()).thenReturn(accounts);

        savingsAccountService.calculateSavingsAccountRatePercent();

        verify(repository).findAll();
    }
}
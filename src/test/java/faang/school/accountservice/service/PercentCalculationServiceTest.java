package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.SavingsAccount;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.SavingsAccountTariffHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PercentCalculationServiceTest {
    @InjectMocks
    private PercentCalculationService percentCalculationService;

    @Mock
    private BalanceService balanceService;

    @Mock
    private SavingsAccountRepository savingsAccountRepository;
    @Mock
    private SavingsAccountTariffHistoryRepository historyRepository;

    private SavingsAccount savingsAccount;
    private TariffDto tariffDto;
    private BalanceDto initialBalance;

    @BeforeEach
    public void setup() {
        savingsAccount = SavingsAccount.builder()
                .id(1L)
                .account(Account.builder().id(1L).build())
                .lastUpdateCalculationAt(LocalDateTime.now().minusDays(2))
                .build();

        tariffDto = TariffDto.builder()
                .id(1L)
                .rate(new BigDecimal("0.05"))
                .build();

        initialBalance = BalanceDto.builder()
                .id(1L)
                .authorizationBalance(new BigDecimal("1000"))
                .actualBalance(new BigDecimal("2000"))
                .build();

        when(savingsAccountRepository.findAll()).thenReturn(new ArrayList<SavingsAccount>() {{
            add(savingsAccount);
        }});
        when(historyRepository.getCurrentTariffAndRateByClientId(1L)).thenReturn(tariffDto);
        when(balanceService.getBalanceByAccountId(1L)).thenReturn(initialBalance);
    }

    @Test
    public void testCalculateInterestAndUpdateBalances() {
        percentCalculationService.calculateInterestAndUpdateBalances();

        BigDecimal interestRate = tariffDto.getRate();
        BigDecimal expectedInterest = initialBalance.getActualBalance().multiply(interestRate);
        BigDecimal expectedFinalBalance = initialBalance.getActualBalance().add(expectedInterest);

        initialBalance.setActualBalance(initialBalance.getActualBalance().add(expectedInterest));

        BalanceDto finalBalance = balanceService.getBalanceByAccountId(1L);

        assertEquals(expectedFinalBalance, finalBalance.getActualBalance(), "Баланс после начисления процентов не соответствует ожиданиям");
    }

    @Test
    public void testCalculateInterestAndUpdateBalancesNoTariffInfo() {
        when(historyRepository.getCurrentTariffAndRateByClientId(1L)).thenReturn(null);

        BigDecimal initialActualBalance = initialBalance.getActualBalance();

        percentCalculationService.calculateInterestAndUpdateBalances();

        BalanceDto finalBalance = balanceService.getBalanceByAccountId(1L);

        assertEquals(initialActualBalance, finalBalance.getActualBalance(), "Баланс изменился, хотя не должен был");
    }

    @Test
    public void testConcurrentCalculateInterestAndUpdateBalances() throws InterruptedException {
        int numberOfAccounts = 10;
        List<SavingsAccount> savingsAccounts = new ArrayList<>();

        for (long i = 1; i <= numberOfAccounts; i++) {
            SavingsAccount savingsAccount = SavingsAccount.builder()
                    .id(i)
                    .account(Account.builder().id(i).build())
                    .lastUpdateCalculationAt(LocalDateTime.now().minusDays(2))
                    .build();

            savingsAccounts.add(savingsAccount);

            when(historyRepository.getCurrentTariffAndRateByClientId(i)).thenReturn(tariffDto);
        }

        when(savingsAccountRepository.findAll()).thenReturn(savingsAccounts);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfAccounts);
        CountDownLatch latch = new CountDownLatch(numberOfAccounts);

        for (SavingsAccount savingsAccount : savingsAccounts) {
            executorService.submit(() -> {
                percentCalculationService.calculateInterestAndUpdateBalances();
                latch.countDown();
            });
        }

        latch.await();

        for (SavingsAccount savingsAccount : savingsAccounts) {
            BigDecimal interestRate = tariffDto.getRate();
            BigDecimal expectedInterest = initialBalance.getActualBalance().multiply(interestRate);
            BigDecimal expectedFinalBalance = initialBalance.getActualBalance().add(expectedInterest);

            initialBalance.setActualBalance(initialBalance.getActualBalance().add(expectedInterest));

            when(balanceService.getBalanceByAccountId(savingsAccount.getId())).thenReturn(initialBalance);

            BalanceDto finalBalance = balanceService.getBalanceByAccountId(savingsAccount.getId());

            assertEquals(expectedFinalBalance, finalBalance.getActualBalance(), "Баланс после начисления процентов не соответствует ожиданиям");
        }
    }
}
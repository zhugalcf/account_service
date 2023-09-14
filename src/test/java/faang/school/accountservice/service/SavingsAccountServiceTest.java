package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.AccountStatus;
import faang.school.accountservice.model.AccountType;
import faang.school.accountservice.model.Currency;
import faang.school.accountservice.model.saving.SavingAccount;
import faang.school.accountservice.model.saving.Tariff;
import faang.school.accountservice.model.saving.TariffType;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.SavingAccountRepository;
import faang.school.accountservice.repository.TariffRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.Objects;

@SpringBootTest
public class SavingsAccountServiceTest {
    @Autowired
    SavingsAccountService savingsAccountService;
    @Autowired
    SavingAccountRepository savingAccountRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TariffRepository tariffRepository;

    Account account;
    Tariff tariff;
    SavingsAccountDto savingsAccountDto;
    SavingAccount savingAccount;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:13.3"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .number("1234567890875")
                .currency(Currency.USD)
                .status(AccountStatus.ACTIVE)
                .type(AccountType.SAVINGS_ACCOUNT)
                .userId(1L)
                .build();
        tariff = Tariff.builder()
                .tariffType(TariffType.GOLD)
                .currentRate(12)
                .build();
        account = accountRepository.save(account);
        tariff = tariffRepository.save(tariff);
        savingAccount = SavingAccount.builder()
                .account(account)
                .balance(new BigDecimal(1000))
                .current_tariff(tariff)
                .build();

        savingsAccountDto = SavingsAccountDto.builder()
                .accountId(account.getId())
                .balance(new BigDecimal(1000))
                .current_tariff(tariff.getId())
                .build();
    }

    @Test
    void openAccountTest() {
        var savingAccount = savingsAccountService.openAccount(savingsAccountDto);
        Assertions.assertNotNull(savingAccountRepository.findById(savingAccount.getId()).get());
    }

    @Test
    void getSavingAccountTest() {
        var account = savingAccountRepository.save(savingAccount);
        var res = savingsAccountService.getSavingAccount(account.getId());
        Assertions.assertEquals(res.getTariffDto().getCurrentRate(), tariff.getCurrentRate());
    }

    @Test
    void updateInterestTest() {
        var account = savingAccountRepository.save(savingAccount);
        savingsAccountService.updateInterest(1);
        var res = savingAccountRepository.findById(account.getId());
        Assertions.assertTrue(!Objects.equals(res.get().getBalance(), account.getBalance()));
    }
}

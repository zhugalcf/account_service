package faang.school.accountservice.controller;

import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.RateRepository;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.TariffHistoryRepository;
import faang.school.accountservice.repository.TariffRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SavingsAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TariffRepository tariffRepository;
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private TariffHistoryRepository tariffHistoryRepository;

    private SavingsAccount savingsAccount;

    private final String accountNumber = "55360000000000000001";

    @Container
    public static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("postgres")
                    .withUsername("username")
                    .withPassword("password");


    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
    }

    @AfterEach
    void delete() {
        tariffHistoryRepository.deleteAll();
        rateRepository.deleteAll();
        tariffRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void testContainerWorksTest() {
        assertThat(postgresContainer.isCreated()).isTrue();
        assertThat(postgresContainer.isRunning()).isTrue();
    }

    @Test
    public void changeSavingsAccountTariffTest() throws Exception {
        initPutEndpoint();

        String jsonRequestBody = "{\"savingsAccountId\": 1, \"tariffType\": \"PROMO\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/savings/account/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpectAll(
                        jsonPath("$.id").value(1),
                        jsonPath("$.accountNumber").value(accountNumber),
                        jsonPath("$.balance").value(0),
                        jsonPath("$.accountId").value(1),
                        jsonPath("$.version").value(2),
                        jsonPath("$.tariffDto.id").value(2),
                        jsonPath("$.tariffDto.type").value("PROMO"),
                        jsonPath("$.tariffDto.ratePercent").value(1.7)
                );
        TariffHistory tariffHistory = tariffHistoryRepository.findById(2L).orElse(null);
        assertNotNull(tariffHistory);
    }

    @Test
    public void openSavingsAccountTest() throws Exception {
        initPostEndpoint();

        String jsonRequestBody = "{\"accountId\": 2, \"tariffType\": \"BASIC\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/savings/account/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpectAll(
                        jsonPath("$.id").value(2),
                        jsonPath("$.accountNumber").value(accountNumber),
                        jsonPath("$.balance").value(0),
                        jsonPath("$.accountId").value(2),
                        jsonPath("$.version").value(1),
                        jsonPath("$.tariffDto.id").value(3),
                        jsonPath("$.tariffDto.type").value("BASIC"),
                        jsonPath("$.tariffDto.ratePercent").value(3.5)
                );

        SavingsAccount savingsAccount = savingsAccountRepository.findById(2L).orElse(null);
        TariffHistory tariffHistory = tariffHistoryRepository.findById(3L).orElse(null);
        Tariff tariff = tariffRepository.findById(3L).orElse(null);
        Rate rate = rateRepository.findById(3L).orElse(null);

        assertAll(
                () -> assertNotNull(savingsAccount),
                () -> assertNotNull(tariff),
                () -> assertNotNull(rate),
                () -> assertNotNull(tariffHistory),
                () -> assertEquals(TariffType.BASIC, tariff.getType()),
                () -> assertEquals(3.5, rate.getPercent()),
                () -> assertEquals(accountNumber, savingsAccount.getAccountNumber()),
                () -> assertEquals(BigDecimal.valueOf(0, 2), savingsAccount.getBalance())
        );
    }

    @Test
    public void getSavingsAccountByIdTest() throws Exception {
        initGetEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/savings/account/{id}", 3))
                .andExpectAll(
                        jsonPath("$.id").value(3),
                        jsonPath("$.accountNumber").value(accountNumber),
                        jsonPath("$.balance").value(0),
                        jsonPath("$.accountId").value(3),
                        jsonPath("$.version").value(1),
                        jsonPath("$.tariffDto.id").value(4),
                        jsonPath("$.tariffDto.type").value("BASIC"),
                        jsonPath("$.tariffDto.ratePercent").value(3.5)
                );
    }

    @Test
    public void getSavingsAccountByOwnerIdAndOwnerTypeTest() throws Exception {
        initGetEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/savings/account")
                        .param("ownerId", String.valueOf(1))
                        .param("ownerType", "USER")
                )
                .andExpectAll(
                        jsonPath("$.id").value(4),
                        jsonPath("$.accountNumber").value(accountNumber),
                        jsonPath("$.balance").value(0),
                        jsonPath("$.accountId").value(4),
                        jsonPath("$.version").value(1),
                        jsonPath("$.tariffDto.id").value(5),
                        jsonPath("$.tariffDto.type").value("BASIC"),
                        jsonPath("$.tariffDto.ratePercent").value(3.5)
                );
    }

//    @Test
//    public void addFundsToSavingsAccountTest() throws Exception {
//        initGetEndpoint();
//
//        String jsonRequestBody = "{\"savingsAccountId\": 5, \"moneyAmount\": 137.35}";
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/savings/account/funds")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequestBody))
//                .andExpectAll(
//                        jsonPath("$.id").value(5),
//                        jsonPath("$.accountNumber").value(accountNumber),
//                        jsonPath("$.balance").value(137.35),
//                        jsonPath("$.accountId").value(5),
//                        jsonPath("$.version").value(2),
//                        jsonPath("$.tariffDto.id").value(6),
//                        jsonPath("$.tariffDto.type").value("BASIC"),
//                        jsonPath("$.tariffDto.ratePercent").value(3.5)
//                );
//    }

    private void initPutEndpoint() {
        Tariff basic = Tariff.builder()
                .type(TariffType.BASIC)
                .build();
        Tariff promo = Tariff.builder()
                .type(TariffType.PROMO)
                .build();
        Rate basicRate = Rate.builder()
                .percent(3.5f)
                .tariff(basic)
                .build();
        Rate promoRate = Rate.builder()
                .percent(1.7f)
                .tariff(promo)
                .build();
        Account firstAccount = Account.builder()
                .ownerType(OwnerType.USER)
                .ownerId(1)
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.OPEN)
                .currency(Currency.USD)
                .build();
        SavingsAccount savingsAccount = SavingsAccount.builder()
                .accountNumber(accountNumber)
                .account(firstAccount)
                .balance(BigDecimal.ZERO)
                .version(1)
                .build();
        TariffHistory tariffHistory = TariffHistory.builder()
                .tariff(basic)
                .savingsAccount(savingsAccount)
                .build();

        tariffRepository.saveAll(List.of(basic, promo));
        rateRepository.saveAll(List.of(basicRate, promoRate));
        accountRepository.save(firstAccount);
        savingsAccountRepository.save(savingsAccount);
        tariffHistoryRepository.save(tariffHistory);
    }

    private void initPostEndpoint() {
        Tariff basic = Tariff.builder()
                .type(TariffType.BASIC)
                .build();
        Rate basicRate = Rate.builder()
                .percent(3.5f)
                .tariff(basic)
                .build();
        Account secondAccount = Account.builder()
                .ownerType(OwnerType.USER)
                .ownerId(2)
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.OPEN)
                .currency(Currency.USD)
                .build();
        accountRepository.save(secondAccount);
        tariffRepository.save(basic);
        rateRepository.save(basicRate);
    }

    private void initGetEndpoint() {
        Tariff basic = Tariff.builder()
                .type(TariffType.BASIC)
                .build();
        Rate basicRate = Rate.builder()
                .percent(3.5f)
                .tariff(basic)
                .build();
        Account firstAccount = Account.builder()
                .ownerType(OwnerType.USER)
                .ownerId(1)
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.OPEN)
                .currency(Currency.USD)
                .build();
        SavingsAccount savingsAccount = SavingsAccount.builder()
                .accountNumber(accountNumber)
                .account(firstAccount)
                .balance(BigDecimal.ZERO)
                .version(1)
                .build();
        TariffHistory tariffHistory = TariffHistory.builder()
                .tariff(basic)
                .savingsAccount(savingsAccount)
                .build();

        tariffRepository.save(basic);
        rateRepository.save(basicRate);
        accountRepository.save(firstAccount);
        savingsAccountRepository.save(savingsAccount);
        tariffHistoryRepository.save(tariffHistory);
    }
}
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
import org.junit.jupiter.api.BeforeEach;
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

    private Account firstAccount;
    private Account secondAccount;

    private TariffHistory tariffHistory;

    private Tariff basic;
    private Tariff promo;

    private Rate basicRate;
    private Rate promoRate;

    private SavingsAccount savingsAccount;

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

    @BeforeEach
    void setUp() {
        basic = Tariff.builder()
                .type(TariffType.BASIC)
                .build();
        promo = Tariff.builder()
                .type(TariffType.PROMO)
                .build();
        basicRate = Rate.builder()
                .percent(3.5f)
                .tariff(basic)
                .build();
        promoRate = Rate.builder()
                .percent(1.7f)
                .tariff(promo)
                .build();
        firstAccount = Account.builder()
                .ownerType(OwnerType.USER)
                .ownerId(1)
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.OPEN)
                .currency(Currency.USD)
                .build();
        secondAccount = Account.builder()
                .ownerType(OwnerType.USER)
                .ownerId(2)
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.OPEN)
                .currency(Currency.USD)
                .build();
        savingsAccount = SavingsAccount.builder()
                .account(firstAccount)
                .version(1)
                .build();
        tariffHistory = TariffHistory.builder()
                .tariff(basic)
                .savingsAccount(savingsAccount)
                .build();
        rateRepository.deleteAll();
        tariffRepository.deleteAll();
        tariffHistoryRepository.deleteAll();
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
                        jsonPath("$.accountId").value(1),
                        jsonPath("$.version").value(2),
                        jsonPath("$.tariffDto.id").value(2),
                        jsonPath("$.tariffDto.type").value("PROMO"),
                        jsonPath("$.tariffDto.ratePercent").value(1.7)
                );
        TariffHistory tariffHistory = tariffHistoryRepository.findById(2L).orElse(null);

        assertAll(
                () -> assertNotNull(tariffHistory),
                () -> assertEquals(2, tariffHistory.getId())
        );
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
                () -> assertEquals(3, tariffHistory.getId()),
                () -> assertEquals(2, savingsAccount.getId())
        );
    }

    @Test
    public void getSavingsAccountByIdTest() throws Exception {
        initGetEndpoint();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/savings/account/{id}", 3))
                .andExpectAll(
                        jsonPath("$.id").value(3),
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
                        jsonPath("$.accountId").value(4),
                        jsonPath("$.version").value(1),
                        jsonPath("$.tariffDto.id").value(5),
                        jsonPath("$.tariffDto.type").value("BASIC"),
                        jsonPath("$.tariffDto.ratePercent").value(3.5)
                );
    }

    private void initPutEndpoint() {
        tariffRepository.saveAll(List.of(basic, promo));
        rateRepository.saveAll(List.of(basicRate, promoRate));
        accountRepository.save(firstAccount);
        savingsAccountRepository.save(savingsAccount);
        tariffHistoryRepository.save(tariffHistory);
    }

    private void initPostEndpoint() {
        accountRepository.save(secondAccount);
        tariffRepository.save(basic);
        rateRepository.save(basicRate);
    }

    private void initGetEndpoint() {
        tariffRepository.save(basic);
        rateRepository.save(basicRate);
        accountRepository.save(firstAccount);
        savingsAccountRepository.save(savingsAccount);
        tariffHistoryRepository.save(tariffHistory);
    }
}
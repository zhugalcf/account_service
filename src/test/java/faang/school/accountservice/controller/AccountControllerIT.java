package faang.school.accountservice.controller;

import faang.school.accountservice.config.context.UserContext;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.service.AccountService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Testcontainers
class AccountControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1")
            .withInitScript("db/changelog/changeset/testcontainers/V001__account-service_get.sql");

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.5.3"));

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        kafka.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        kafka.stop();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserContext userContext;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    void get() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/account/1");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("123456789013123"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.type").value("CURRENT_ACCOUNT"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void freeze() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.put("/account/freeze/2");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.number").value("1234567890131232"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.type").value("CURRENT_ACCOUNT"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("FROZEN"));
    }

    @Test
    void close() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.put("/account/close/3");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.number").value("12345678901312"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.type").value("CURRENT_ACCOUNT"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }
}

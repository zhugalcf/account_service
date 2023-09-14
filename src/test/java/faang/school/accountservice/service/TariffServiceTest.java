package faang.school.accountservice.service;

import faang.school.accountservice.dto.TariffDto;

import faang.school.accountservice.model.saving.Tariff;
import faang.school.accountservice.model.saving.TariffType;
import faang.school.accountservice.repository.TariffRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;

@SpringBootTest
public class TariffServiceTest {
    @Autowired
    TariffService tariffService;
    @Autowired
    TariffRepository tariffRepository;

    Tariff tariff;
    TariffDto tariffDto;

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
        tariff = Tariff.builder()
                .tariffType(TariffType.GOLD)
                .currentRate(12)
                .build();
        tariffDto = TariffDto.builder()
                .id(1L)
                .tariffType(TariffType.GOLD)
                .currentRate(12)
                .build();
        tariff = tariffRepository.save(tariff);
    }

    @Test
    void getTariffByIdTest() {
        var res = tariffService.getTariffById(tariff.getId());
        Assertions.assertEquals(res.getCurrentRate(), tariff.getCurrentRate());
    }

    @Test
    void getTariffDtoByIdTest() {
        var res = tariffService.getTariffDtoById(tariff.getId());
        Assertions.assertEquals(tariff.getTariffType(), res.getTariffType());
        Assertions.assertEquals(tariff.getCurrentRate(), res.getCurrentRate());
    }

    @Test
    void createTariffTest() {
        var res = tariffService.createTariff(tariffDto);
        tariffDto.setId(res.getId());
        Assertions.assertEquals(tariffDto, res);
    }
    @Test
    void updateTariffTest() {
        tariffDto.setTariffType(TariffType.PREMIUM);
        var res = tariffService.updateTariff(tariffDto);
        Assertions.assertEquals(tariffDto, res);
        Assertions.assertEquals(tariffRepository.findById(res.getId()).get().getTariffType(),TariffType.PREMIUM);
    }
    @Test
    void updateTariffHistoryTest() {
        var previousHistory = tariffRepository.findById(tariff.getId()).get().getRateHistory();
        var previousRate = tariffDto.getCurrentRate();

        tariffDto.setCurrentRate(100);
        var res = tariffService.updateTariff(tariffDto);
        var newHistory = tariffRepository.findById(res.getId()).get().getRateHistory();
        Assertions.assertNotEquals(previousHistory, newHistory);
        Assertions.assertEquals(newHistory, Arrays.asList(previousRate).toString());
    }
}

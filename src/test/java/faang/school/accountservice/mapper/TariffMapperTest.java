package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.Tariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TariffMapperTest {

    private TariffMapper mapper = new TariffMapperImpl();

    private Tariff tariff;
    private TariffDto tariffDto;
    private Rate rate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BeforeEach
    void setUp() {
        createdAt = LocalDateTime.now().minusMonths(1);
        updatedAt = LocalDateTime.now().minusDays(1);
        tariffDto = TariffDto.builder()
                .id(10)
                .type(TariffType.PROMO)
                .ratePercent(10.5f)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        rate = Rate.builder()
                .percent(10.5f)
                .build();
        tariff = Tariff.builder()
                .id(10)
                .type(TariffType.PROMO)
                .rateHistory(new ArrayList<>(List.of(rate)))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Test
    void toDtoTest() {
        TariffDto result = mapper.toDto(tariff);

        assertEquals(tariffDto, result);
    }

    @Test
    void toEntityTest() {
        Tariff expected = Tariff.builder()
                .id(10)
                .type(TariffType.PROMO)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        Tariff result = mapper.toEntity(tariffDto);

        assertEquals(expected, result);
    }
}
package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.Tariff;
import faang.school.accountservice.entity.TariffType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TariffMapperTest {
    @Spy
    private TariffMapperImpl tariffMapper;
    private Tariff expectedTariff;
    private TariffDto expectedTariffDto;


    @BeforeEach
    void setUp() {
        expectedTariff = Tariff.builder()
                .tariffType(TariffType.BASE)
                .rates(Arrays.asList(BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0)))
                .build();

        expectedTariffDto = TariffDto.builder()
                .tariffType(TariffType.BASE)
                .rates(Arrays.asList(BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0)))
                .build();
    }

    @Test
    void testToTariffDto() {
        TariffDto actualTariffDto = tariffMapper.toTariffDto(expectedTariff);

        assertNotNull(actualTariffDto);
        assertEquals(expectedTariff.getTariffType(), actualTariffDto.getTariffType());
        assertEquals(expectedTariff.getRates(), actualTariffDto.getRates());
    }

    @Test
    void testToTariff() {
        Tariff actualTariff = tariffMapper.toTariff(expectedTariffDto);

        assertNotNull(actualTariff);
        assertEquals(expectedTariffDto.getTariffType(), actualTariff.getTariffType());
        assertEquals(expectedTariffDto.getRates(), actualTariff.getRates());
    }
}
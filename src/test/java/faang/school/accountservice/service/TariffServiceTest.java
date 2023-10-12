package faang.school.accountservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.tariff.TariffCreateDto;
import faang.school.accountservice.dto.tariff.TariffDto;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.repository.TariffRepository;
import faang.school.accountservice.service.savings.TariffService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TariffServiceTest {
    @Mock
    private TariffRepository tariffRepository;
    @InjectMocks
    private TariffService tariffService;
    private final Tariff tariff = new Tariff(0, "Basic", 2.5F, "[]");

    @Test
    void create() {
        TariffCreateDto dto = new TariffCreateDto();
        dto.setTypeTariff("Basic");
        dto.setBet(BigDecimal.valueOf(2.5));
        Mockito.when(tariffRepository.save(tariff)).thenReturn(tariff);
        tariffService.create(dto);
        Mockito.verify(tariffRepository, Mockito.times(1)).save(tariff);
    }

    @Test
    void update() {
        TariffService service = new TariffService(new ObjectMapper(), tariffRepository);
        Mockito.when(tariffRepository.getReferenceById(1L)).thenReturn(tariff);
        TariffDto actual = new TariffDto("Basic", 2.0F, "[2.5]");
        TariffDto expected = service.update(1L, 2.0F);
        Assertions.assertEquals(expected.getBettingHistory(), actual.getBettingHistory());
    }

    @Test
    void getAll() {
        Mockito.when(tariffRepository.findAll()).thenReturn(List.of(new Tariff(), new Tariff()));
        List<TariffDto> list = tariffService.getAll();
        Assertions.assertEquals(list.size(), 2);
        Mockito.verify(tariffRepository, Mockito.times(1)).findAll();
    }
}
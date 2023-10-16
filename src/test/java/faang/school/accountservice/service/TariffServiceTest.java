package faang.school.accountservice.service;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.Tariff;
import faang.school.accountservice.exception.TariffNotFoundException;
import faang.school.accountservice.mapper.TariffMapper;
import faang.school.accountservice.repository.TariffRepository;
import faang.school.accountservice.validator.TariffValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import faang.school.accountservice.entity.TariffType;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TariffServiceTest {
    @InjectMocks
    private TariffService tariffService;
    @Mock
    private TariffValidator tariffValidator;
    @Mock
    private TariffRepository tariffRepository;
    @Mock
    private TariffMapper tariffMapper;
    private Tariff tariff;
    private TariffDto dto;
    private Long tariffId;

    @BeforeEach
    void setUp() {
        tariffId = 1L;
        BigDecimal rate = new BigDecimal("0.5");

        tariff = Tariff.builder()
                .id(tariffId)
                .tariffType(TariffType.BASE)
                .rates(List.of(BigDecimal.valueOf(0.5)))
                .build();

        dto = TariffDto.builder()
                .id(tariffId)
                .tariffType(TariffType.BASE)
                .rate(rate)
                .build();

        when(tariffRepository.findById(tariffId)).thenReturn(Optional.of(tariff));
        when(tariffRepository.save(any(Tariff.class))).thenReturn(tariff);
        when(tariffMapper.toTariffDto(tariff)).thenReturn(dto);
        when(tariffMapper.toTariff(dto)).thenReturn(tariff);
    }

    @Test
    public void getTariffById_ExistingId_ShouldReturnTariffDto() {
        TariffDto resultTariffDto = tariffService.getTariffById(tariffId);

        assertEquals(dto, resultTariffDto);
    }

    @Test
    public void createTariff_ValidTariffDto_ShouldCreateTariff() {
        TariffDto createdTariff = tariffService.createTariff(dto);

        verify(tariffValidator, times(1)).validateTariff(dto);
        verify(tariffRepository, times(1)).save(any(Tariff.class));
    }

    @Test
    void testUpdateTariff() {
        TariffDto result = tariffService.updateTariff(1L, dto);

        verify(tariffRepository, times(1)).findById(1L);
        verify(tariffMapper, times(1)).toTariffDto(tariff);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getTariffType(), result.getTariffType());
        assertEquals(dto.getRate(), result.getRate());
    }

    @Test
    void testUpdateTariff_WhenTariffNotFound() {
        Long nonExistentTariffId = 123L;

        assertThrows(TariffNotFoundException.class, () -> tariffService.updateTariff(nonExistentTariffId, dto));
    }

    @Test
    public void testUpdateTariffRate() {
        BigDecimal newRate = new BigDecimal("0.10");

        Tariff existingTariff = new Tariff();
        existingTariff.setId(tariffId);
        existingTariff.setRates(new ArrayList<>());

        when(tariffRepository.findById(tariffId)).thenReturn(Optional.of(existingTariff));

        tariffService.updateTariffRate(tariffId, newRate);

        verify(tariffRepository, times(1)).save(existingTariff);

        assertEquals(1, existingTariff.getRates().size());
        assertEquals(newRate, existingTariff.getRates().get(0));
    }

    @Test
    void testUpdateTariffRate_WhenTariffNotFound() {
        Long nonExistentTariffId = 123L;
        BigDecimal newRate = new BigDecimal("0.10");

        assertThrows(TariffNotFoundException.class, () -> tariffService.updateTariffRate(nonExistentTariffId, newRate));
    }

    @Test
    void deleteTariff_ExistingTariff_ShouldDeleteTariff() {
        tariffService.deleteTariff(tariffId);

        verify(tariffRepository, times(1)).delete(tariff);
    }

    @Test
    void deleteTariff_NonExistentTariff_ShouldThrowNotFoundException() {
        when(tariffRepository.findById(tariffId)).thenReturn(java.util.Optional.empty());

        assertThrows(TariffNotFoundException.class, () -> {
            tariffService.deleteTariff(tariffId);
        });

        verify(tariffRepository, never()).delete(any(Tariff.class));
    }

    @Test
    void getAllTariffs_ShouldReturnListOfTariffDto() {
        Tariff tariff1 = new Tariff();
        tariff1.setId(1L);
        tariff1.setTariffType(TariffType.BASE);
        tariff1.setRates(List.of(BigDecimal.valueOf(0.5)));

        Tariff tariff2 = new Tariff();
        tariff2.setId(2L);
        tariff2.setTariffType(TariffType.PROMO);
        tariff2.setRates(List.of(BigDecimal.valueOf(0.8)));

        List<Tariff> tariffs = List.of(tariff1, tariff2);

        TariffDto tariffDto1 = new TariffDto();
        tariffDto1.setId(1L);
        tariffDto1.setTariffType(TariffType.BASE);
        tariffDto1.setRate(BigDecimal.valueOf(0.5));

        TariffDto tariffDto2 = new TariffDto();
        tariffDto2.setId(2L);
        tariffDto2.setTariffType(TariffType.PROMO);
        tariffDto2.setRate(BigDecimal.valueOf(0.8));

        List<TariffDto> expectedTariffDtos = List.of(tariffDto1, tariffDto2);

        when(tariffRepository.findAll()).thenReturn(tariffs);
        when(tariffMapper.toTariffDto(tariff1)).thenReturn(tariffDto1);
        when(tariffMapper.toTariffDto(tariff2)).thenReturn(tariffDto2);

        List<TariffDto> resultTariffDtos = tariffService.getAllTariffs();

        assertEquals(expectedTariffDtos, resultTariffDtos);

        verify(tariffRepository, times(1)).findAll();

        verify(tariffMapper, times(1)).toTariffDto(tariff1);
        verify(tariffMapper, times(1)).toTariffDto(tariff2);
    }

    @Test
    void getAllTariffs_EmptyList_ShouldReturnEmptyList() {
        when(tariffRepository.findAll()).thenReturn(new ArrayList<>());

        List<TariffDto> resultTariffDtos = tariffService.getAllTariffs();

        assertEquals(new ArrayList<>(), resultTariffDtos);

        verify(tariffRepository, times(1)).findAll();

        verify(tariffMapper, never()).toTariffDto(any(Tariff.class));
    }
}
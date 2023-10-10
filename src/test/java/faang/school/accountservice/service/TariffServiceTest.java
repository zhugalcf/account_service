package faang.school.accountservice.service;

import faang.school.accountservice.dto.AddTariffDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.dto.UpdateTariffDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.exception.RateAlreadyAssignedException;
import faang.school.accountservice.mapper.TariffMapper;
import faang.school.accountservice.mapper.TariffMapperImpl;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.repository.TariffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TariffServiceTest {

    @Mock
    private RateService rateService;
    @Mock
    private TariffRepository tariffRepository;
    @Mock
    private TariffHistoryService tariffHistoryService;
    @Spy
    private TariffMapper tariffMapper = new TariffMapperImpl();
    @InjectMocks
    private TariffService tariffService;

    private Tariff tariffWithoutRate;
    private Tariff tariffWithRate;

    private UpdateTariffDto updateTariffDto;

    private TariffDto tariffWithoutRateDto;
    private TariffDto tariffWithRateDto;

    private Rate twoAndFiveRate;
    private Rate threeAndFiveRate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private AddTariffDto addTariffDto;

    @BeforeEach
    void setUp() {
        createdAt = LocalDateTime.now().minusMonths(1);
        updatedAt = LocalDateTime.now().minusDays(1);
        twoAndFiveRate = Rate.builder()
                .percent(2.5f)
                .build();
        threeAndFiveRate = Rate.builder()
                .percent(3.5f)
                .build();
        tariffWithoutRate = Tariff.builder()
                .type(TariffType.BASIC)
                .build();
        tariffWithRate = Tariff.builder()
                .type(TariffType.BASIC)
                .rateHistory(new ArrayList<>(List.of(twoAndFiveRate)))
                .build();
        updateTariffDto = UpdateTariffDto.builder()
                .tariffId(10)
                .ratePercent(3.5f)
                .build();
        tariffWithoutRateDto = TariffDto.builder()
                .type(TariffType.BASIC)
                .build();
        tariffWithRateDto = TariffDto.builder()
                .type(TariffType.BASIC)
                .ratePercent(2.5f)
                .build();
        addTariffDto = AddTariffDto.builder()
                .type(TariffType.BASIC)
                .ratePercent(2.5f)
                .build();
    }

    @Test
    void addTariffTest() {
        when(tariffRepository.save(any(Tariff.class))).thenReturn(tariffWithoutRate);
        when(rateService.assignRatePercentToTariff(2.5f, tariffWithoutRate)).thenReturn(twoAndFiveRate);

        TariffDto result = tariffService.addTariff(addTariffDto);

        assertEquals(tariffWithRateDto, result);

        verify(tariffRepository).save(any(Tariff.class));
        verify(rateService).assignRatePercentToTariff(2.5f, tariffWithoutRate);
    }

    @Test
    void updateTariffRateFirstScenarioTest() {
        when(tariffRepository.findById(10L)).thenReturn(Optional.of(tariffWithRate));
        when(rateService.assignRatePercentToTariff(3.5f, tariffWithRate)).thenReturn(threeAndFiveRate);

        TariffDto result = tariffService.updateTariffRate(updateTariffDto);

        assertEquals(3.5f, result.getRatePercent());

        verify(tariffRepository).findById(10L);
        verify(rateService).assignRatePercentToTariff(3.5f, tariffWithRate);
    }

    @Test
    void updateTariffRateSecondScenarioTest() {
        when(tariffRepository.findById(10L)).thenReturn(Optional.of(tariffWithRate));
        updateTariffDto.setRatePercent(2.5f);

        RateAlreadyAssignedException exception = assertThrows(RateAlreadyAssignedException.class,
                () -> tariffService.updateTariffRate(updateTariffDto));

        assertEquals("Rate: 2,500, already assigned to tariff with ID: 10", exception.getMessage());
    }

    @Test
    void getTariffByFirstTest() {
        when(tariffRepository.findById(4L)).thenReturn(Optional.of(tariffWithRate));

        TariffDto result = tariffService.getTariffDtoBy(4);

        assertEquals(tariffWithRateDto, result);

        verify(tariffRepository).findById(4L);
    }

    @Test
    void getTariffBySecondTest() {
        when(tariffRepository.getByType(TariffType.BASIC)).thenReturn(Optional.of(tariffWithRate));

        Tariff result = tariffService.getTariffDtoBy(TariffType.BASIC);

        assertEquals(tariffWithRate, result);

        verify(tariffRepository).getByType(TariffType.BASIC);
    }

    @Test
    void getTariffsTest() {
        Pageable pageable = PageRequest.of(0, 2);

        List<Tariff> tariffs = List.of(tariffWithRate);
        Page<TariffDto> expected = new PageImpl<>(List.of(tariffWithRateDto));

        Page<Tariff> tariffPage = new PageImpl<>(tariffs, pageable, tariffs.size());

        when(tariffRepository.findAll(pageable)).thenReturn(tariffPage);

        Page<TariffDto> result = tariffService.getTariffs(pageable);

        assertEquals(expected, result);

        verify(tariffRepository).findAll(pageable);
    }

    @Test
    void assignTariffToSavingsAccountTest() {
        SavingsAccount savingsAccount = SavingsAccount.builder().build();

        TariffHistory tariffHistory = TariffHistory.builder()
                .savingsAccount(savingsAccount)
                .tariff(tariffWithRate)
                .build();

        when(tariffRepository.getByType(TariffType.BASIC)).thenReturn(Optional.of(tariffWithRate));
        when(tariffHistoryService.saveTariffHistory(tariffHistory)).thenReturn(tariffHistory);

        TariffHistory result = tariffService.assignTariffToSavingsAccount(savingsAccount, TariffType.BASIC);

        assertEquals(tariffHistory, result);

        verify(tariffRepository).getByType(TariffType.BASIC);
        verify(tariffHistoryService).saveTariffHistory(tariffHistory);
    }

    @Test
    void findTariffByTest() {
        when(tariffRepository.findById(1L)).thenReturn(Optional.of(tariffWithRate));

        Tariff result = tariffService.getTariffBy(1);

        assertEquals(tariffWithRate, result);
        verify(tariffRepository).findById(1L);
    }
}
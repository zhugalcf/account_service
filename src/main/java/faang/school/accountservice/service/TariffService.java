package faang.school.accountservice.service;

import faang.school.accountservice.dto.AddTariffDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.dto.UpdateTariffDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.exception.RateAlreadyAssignedException;
import faang.school.accountservice.exception.TariffNotFoundException;
import faang.school.accountservice.mapper.TariffMapper;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffService {

    private final RateService rateService;
    private final TariffRepository tariffRepository;
    private final TariffHistoryService tariffHistoryService;
    private final TariffMapper tariffMapper;

    @Transactional
    public TariffDto addTariff(AddTariffDto tariffDto) {
        TariffType type = tariffDto.getType();
        float percent = tariffDto.getRatePercent();

        Tariff tariff = Tariff.builder()
                .type(type)
                .build();
        Tariff newTariff = tariffRepository.save(tariff);
        log.info("Saved new tariff with type: {}", type);

        Rate assignedRate = rateService.assignRatePercentToTariff(percent, newTariff);
        log.info("Rate with percent: {}, are assigned to tariff: {}", percent, tariff.getType());

        newTariff.setRateHistory(new ArrayList<>(List.of(assignedRate)));

        return tariffMapper.toDto(newTariff);
    }

    @Transactional
    public TariffDto updateTariffRate(UpdateTariffDto tariffDto) {
        long tariffId = tariffDto.getTariffId();
        float newRatePercent = tariffDto.getRatePercent();

        Tariff tariff = getTariffBy(tariffId);
        float currentTariffRate = getTariffCurrentRate(tariff).getPercent();

        if (currentTariffRate != newRatePercent) {
            Rate newRate = rateService.assignRatePercentToTariff(newRatePercent, tariff);
            log.info("Updated tariff type: {} with new rate: {}%", tariff.getType(), newRatePercent);

            tariff.getRateHistory().add(newRate);
            tariff.setUpdatedAt(LocalDateTime.now());
            return tariffMapper.toDto(tariff);
        }
        throw new RateAlreadyAssignedException(String.format("Rate: %.3f, already assigned to tariff with ID: %d", newRatePercent, tariffId));
    }

    public TariffDto getTariffDtoBy(long id) {
        return tariffMapper.toDto(getTariffBy(id));
    }

    public Tariff getTariffDtoBy(TariffType tariffType) {
        log.info("Received request to get an tariff with type = {}", tariffType);
        return tariffRepository.getByType(tariffType)
                .orElseThrow(() -> new TariffNotFoundException(String.format("Tariff of the specified type: %s does not exist", tariffType)));
    }

    public Page<TariffDto> getTariffs(Pageable pageable) {
        Page<Tariff> page = tariffRepository.findAll(pageable);
        List<TariffDto> responseTariffs = page.get()
                .map(tariffMapper::toDto)
                .toList();

        return new PageImpl<>(responseTariffs);
    }

    @Transactional
    public TariffHistory assignTariffToSavingsAccount(SavingsAccount savingsAccount, TariffType tariffType) {
        Tariff tariff = getTariffDtoBy(tariffType);
        log.info("The request to retrieve a tariff of type: {}} was successful", tariffType);

        TariffHistory tariffHistory = TariffHistory.builder()
                .savingsAccount(savingsAccount)
                .tariff(tariff)
                .build();
        return tariffHistoryService.saveTariffHistory(tariffHistory);
    }

    public Tariff getTariffBy(long id) {
        return tariffRepository.findById(id)
                .orElseThrow(() -> new TariffNotFoundException(String.format("Tariff of the specified id: %d does not exist", id)));
    }

    private Rate getTariffCurrentRate(Tariff tariff) {
        int currentRateIndex = tariff.getRateHistory().size() - 1;
        return tariff.getRateHistory().get(currentRateIndex);
    }
}

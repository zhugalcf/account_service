package faang.school.accountservice.service;

import faang.school.accountservice.validator.TariffValidator;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.Tariff;
import faang.school.accountservice.exception.TariffNotFoundException;
import faang.school.accountservice.mapper.TariffMapper;
import faang.school.accountservice.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository tariffRepository;
    private final TariffValidator tariffValidator;
    private final TariffMapper tariffMapper;

    public TariffDto createTariff(TariffDto tariffDto) {
        tariffValidator.validateTariff(tariffDto);
        log.info("Creating new tariff: {}", tariffDto);
        Tariff tariff = tariffMapper.toTariff(tariffDto);

        return tariffMapper.toTariffDto(tariffRepository.save(tariff));
    }

    public TariffDto getTariffById(Long id) {
        log.info("Getting tariff by ID: {}", id);
        Optional<Tariff> tariff = tariffRepository.findById(id);
        return tariff.map(tariffMapper::toTariffDto)
                .orElseThrow(() -> new TariffNotFoundException("Tariff with id " + id + " not found"));
    }

    public List<TariffDto> getAllTariffs() {
        log.info("Getting all tariffs");
        List<Tariff> tariffs = tariffRepository.findAll();
        return tariffs.stream()
                .map(tariffMapper::toTariffDto)
                .collect(Collectors.toList());
    }

    public TariffDto updateTariff(Long id, TariffDto tariffDto) {
        log.info("Updating tariff with ID {}: {}", id, tariffDto);
        Tariff existingTariff = tariffRepository.findById(id)
                .orElseThrow(() -> new TariffNotFoundException("Tariff not found"));

        Tariff updatedTariff = tariffMapper.toTariff(tariffDto);
        updatedTariff.setId(existingTariff.getId());
        Tariff savedTariff = tariffRepository.save(updatedTariff);

        return tariffMapper.toTariffDto(savedTariff);
    }

    public Tariff updateTariffRate(Long tariffId, BigDecimal newRate) {
        log.info("Updating rate with tariff ID {}: {}", tariffId, newRate);
        Tariff existingTariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new TariffNotFoundException("Tariff not found"));

        Tariff updatedTariff = Tariff.builder()
                .tariffType(existingTariff.getTariffType())
                .rates(new ArrayList<>(existingTariff.getRates()))
                .build();
        updatedTariff.getRates().add(newRate);

        tariffRepository.save(existingTariff);
        return tariffRepository.save(updatedTariff);
    }

    public void deleteTariff(Long id) {
        log.info("Deleting tariff with ID: {}", id);
        tariffRepository.deleteById(id);
    }
}
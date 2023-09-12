package faang.school.accountservice.service;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.model.saving.Tariff;
import faang.school.accountservice.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TariffService {
    private final TariffRepository tariffRepository;
    private final AccountMapper mapper;

    public Tariff getTariffById(long id) {
        return tariffRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No tariff with id: " + id)
        );
    }
    public TariffDto getTariffDtoById(long id) {
        var tariff = tariffRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No tariff with id: " + id)
        );
        return mapper.toDto(tariff);
    }
    public TariffDto createTariff(TariffDto tariffDto) {
        var tariff = tariffRepository.save(mapper.toEntity(tariffDto));
        return mapper.toDto(tariffRepository.save(tariff));
    }
    public TariffDto updateTariff(TariffDto tariffDto) {
        var tariff = tariffRepository.findById(tariffDto.getId()).orElseThrow(
                () -> new RuntimeException("No tariff with id: " + tariffDto.getId())
        );
        tariff.setTariffType(tariffDto.getTariffType());
        tariff.setCurrentRate(tariffDto.getCurrentRate());
        return mapper.toDto(tariff);
    }
}
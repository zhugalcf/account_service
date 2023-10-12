package faang.school.accountservice.service.savings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import faang.school.accountservice.dto.tariff.TariffCreateDto;
import faang.school.accountservice.dto.tariff.TariffDto;
import faang.school.accountservice.mapper.TariffMapper;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffService {
    private final ObjectMapper objectMapper;
    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper = TariffMapper.INSTANCE;

    public TariffDto create(TariffCreateDto createDto) {
        Tariff tariff = Tariff.builder().typeTariff(createDto.getTypeTariff())
                .bet(createDto.getBet().floatValue()).bettingHistory("[]").build();
        Tariff newTariff = tariffRepository.save(tariff);
        return new TariffDto(newTariff.getTypeTariff(), newTariff.getBet(), newTariff.getBettingHistory());
    }

    public TariffDto update(long tariffId, float bet) {
        Tariff tariff = tariffRepository.getReferenceById(tariffId);
        if (bet == tariff.getBet()) {
            throw new RuntimeException("the same rate");
        }
        List<Float> tariffHistory = new ArrayList<>();
        try {
            tariffHistory = objectMapper.readValue(tariff.getBettingHistory(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.info(e.toString());
        }
        tariffHistory.add(tariff.getBet());
        tariff.setBet(bet);
        tariff.setBettingHistory(new Gson().toJson(tariffHistory));
        tariffRepository.save(tariff);
        return tariffMapper.tariffToTariffDto(tariff);
    }

    public List<TariffDto> getAll() {
        return tariffRepository.findAll().stream().map(tariffMapper::tariffToTariffDto).toList();
    }
}

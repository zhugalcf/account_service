package faang.school.accountservice.service;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.mapper.TariffMapper;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffService {

    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper;

    public void addTariffToSavingsAccount(long savingsAccountId, TariffType tariffType) {

    }

//    private Tariff getTariffWithCurrentRateBy(TariffType tariffType) {
//
//    }

    public Page<TariffDto> getTariffs(Pageable pageable) {
        Page<Tariff> page = tariffRepository.findAll(pageable);
        List<TariffDto> responseTariffs = page.get()
                .map(tariffMapper::toDto)
                .toList();
        return new PageImpl<>(responseTariffs);
    }
}

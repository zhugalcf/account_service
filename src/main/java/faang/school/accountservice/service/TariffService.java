package faang.school.accountservice.service;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.exception.TariffNotFoundException;
import faang.school.accountservice.mapper.TariffMapper;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffService {

    private SavingsAccountService savingsAccountService;
    private final TariffRepository tariffRepository;
    private final TariffHistoryService tariffHistoryService;
    private final TariffMapper tariffMapper;

    public void addTariffToSavingsAccount(long savingsAccountId, TariffType tariffType) {

    }

    public Tariff getTariff(TariffType tariffType) {
        log.info("Received request to get an tariff with type = {}", tariffType);
        return tariffRepository.getByType(tariffType)
                .orElseThrow(() -> new TariffNotFoundException(String.format("Tariff of the specified type: %s does not exist", tariffType)));
    }

    @Transactional
    public TariffHistory assignTariffToSavingsAccount(SavingsAccount savingsAccount, TariffType tariffType) {
        Tariff tariff = getTariff(tariffType);
        log.info("The request to retrieve a tariff of type: {}} was successful", tariffType);

        TariffHistory tariffHistory = TariffHistory.builder()
                .savingsAccount(savingsAccount)
                .tariff(tariff)
                .build();
        tariffHistoryService.saveTariffHistory(tariffHistory);
        log.info("Tariff with type: {}, has been successfully assigned to savings account", tariffType);
        return tariffHistory;
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

package faang.school.accountservice.service;

import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.repository.TariffHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffHistoryService {

    private final TariffHistoryRepository tariffHistoryRepository;

    @Transactional
    public TariffHistory saveTariffHistory(TariffHistory tariffHistory) {
        return tariffHistoryRepository.save(tariffHistory);
    }
}

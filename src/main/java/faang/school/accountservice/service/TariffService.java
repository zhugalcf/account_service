package faang.school.accountservice.service;

import faang.school.accountservice.model.saving.Tariff;
import faang.school.accountservice.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TariffService {
    private final TariffRepository tariffRepository;

    public Tariff getTariffById(long id) {
        return tariffRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No tariff with id: " + id)
        );
    }
}
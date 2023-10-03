package faang.school.accountservice.service;

import faang.school.accountservice.entity.account.Currency;
import faang.school.accountservice.exception.EntityNotFoundException;
import faang.school.accountservice.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public Currency getCurrency(String code) {
        return currencyRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Currency with code " + code + " not found"));
    }
}

package faang.school.accountservice.service;

import faang.school.accountservice.dto.CurrencyDto;
import faang.school.accountservice.entity.Currency;
import faang.school.accountservice.mapper.CurrencyMapper;
import faang.school.accountservice.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    @Transactional
    public CurrencyDto create(CurrencyDto currency) {
        Currency entity = currencyRepository.save(currencyMapper.toEntity(currency));
        return currencyMapper.toDto(entity);
    }

}

package faang.school.accountservice.controller;

import faang.school.accountservice.dto.CurrencyDto;
import faang.school.accountservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping
    public CurrencyDto create(CurrencyDto currency) {
        return currencyService.create(currency);
    }
}

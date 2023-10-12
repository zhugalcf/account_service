package faang.school.accountservice.controller;

import faang.school.accountservice.dto.tariff.TariffCreateDto;
import faang.school.accountservice.dto.tariff.TariffDto;
import faang.school.accountservice.service.savings.AccrualToScore;
import faang.school.accountservice.service.savings.TariffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts/tariff")
public class TariffController {
    private final TariffService tariffService;
    private final AccrualToScore accrual;

    @GetMapping("/open")
    public TariffDto openTariff(@Valid @RequestBody TariffCreateDto createDto) {
        return tariffService.create(createDto);
    }

    @GetMapping("/update")
    public TariffDto update(@RequestParam(name = "id") long tariffId, @RequestParam float bet) {
        return tariffService.update(tariffId, bet);
    }

    @GetMapping("/all")
    public List<TariffDto> all() {
        return tariffService.getAll();
    }
}

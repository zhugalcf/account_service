package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AddTariffDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.dto.UpdateTariffDto;
import faang.school.accountservice.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tariff")
@Slf4j
public class TariffController {

    private final TariffService tariffService;

    @PostMapping("/add")
    public TariffDto addTariff(@RequestBody @Validated AddTariffDto tariffDto) {
        log.info("Received a request to create tariff with type: {}, and rate percent: {}", tariffDto.getType(), tariffDto.getRatePercent());
        return tariffService.addTariff(tariffDto);
    }

    @PutMapping
    public TariffDto updateTariffRate(@RequestBody UpdateTariffDto tariffDto) {
        log.info("Received a request to update a tariff with ID: {}, to a new rate: {}", tariffDto.getTariffId(), tariffDto.getRatePercent());
        return tariffService.updateTariffRate(tariffDto);
    }

    @GetMapping("/{id}")
    public TariffDto getTariffBy(@PathVariable long id) {
        log.info("Received a request to retrieve a tariff with ID: {}", id);
        return tariffService.getTariffDtoBy(id);
    }

    @GetMapping
    public Page<TariffDto> getTariffs(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Received a request to retrieve objects with a size of: {}", pageable.getPageSize());
        return tariffService.getTariffs(pageable);
    }
}

package faang.school.accountservice.controller;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tariff")
@Slf4j
public class TariffController {

    private final TariffService tariffService;

//    public TariffDto getTariffById(long id){
//
//    }

    public TariffDto getCurrentTariffByType(TariffType tariffType){
        return null;
    }

//    public void addTariffToSavingsAccount(long savingsAccountId, TariffType tariffType){
//        tariffType.addTariffToSavingsAccount(savingsAccountId, tariffType);
//    }

    @GetMapping("/tariffs")
    public Page<TariffDto> getTariffs(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Received a request to retrieve objects with a size of: {}", pageable.getPageSize());
        return tariffService.getTariffs(pageable);
    }
}

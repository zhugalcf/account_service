package faang.school.accountservice.controller;

import faang.school.accountservice.dto.OpenSavingsScoreDto;
import faang.school.accountservice.dto.tariff.TariffDto;
import faang.school.accountservice.service.savings.SavingsAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/accounts/savings-account")
public class SavingsAccountsController {
    private final SavingsAccountsService savingsAccountsService;

    @GetMapping("/open")
    public OpenSavingsScoreDto open() {
        return savingsAccountsService.openScore();
    }

    @GetMapping("/invoice-rate/{scoreId}")
    public TariffDto invoiceRate(@PathVariable long scoreId) {
        return savingsAccountsService.getTariffScore(scoreId);
    }
}

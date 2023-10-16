package faang.school.accountservice.controller;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.service.SavingsAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/savings-account")
@Tag(name = "Savings Account", description = "Savings Account operations")
public class SavingsAccountController {
    private final SavingsAccountService savingsAccountService;

    @PostMapping("/open")
    @Operation(summary = "Create savings account")
    public SavingsAccountDto openSavingsAccount(@RequestParam Long ownerId, @RequestParam String currencyCode) {
        SavingsAccountDto accountDto = savingsAccountService.openSavingsAccount(ownerId, currencyCode);
        log.info("Savings account opened successfully with id {}", accountDto.getId());
        return accountDto;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get savings account by account id")
    public SavingsAccountDto get(@PathVariable Long id) {
        log.info("Savings account retrieved successfully for id {}", id);
        return savingsAccountService.get(id);
    }

    @GetMapping("/{clientId}/current-tariff-and-rate")
    public TariffDto getCurrentTariffAndRateByClientId(@PathVariable Long clientId) {
        log.info("Fetching current tariff and rate for clientId: {}", clientId);
        return savingsAccountService.getCurrentTariffAndRateByClientId(clientId);
    }
}
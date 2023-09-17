package faang.school.accountservice.controller;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{accountId}")
    public BalanceDto getBalance(@PathVariable Long accountId) {
        return balanceService.getBalanceByAccountId(accountId);
    }

    @PostMapping("/{accountId}")
    public BalanceDto createBalance(@PathVariable Long accountId, @Valid @RequestBody BalanceDto balanceDto) {
        return balanceService.createBalance(accountId, balanceDto);
    }

    @PutMapping("/{accountId}")
    public BalanceDto updateBalance(@PathVariable Long accountId, @Valid @RequestBody BalanceDto balanceDto) {
        return balanceService.updateBalance(accountId, balanceDto);
    }
}


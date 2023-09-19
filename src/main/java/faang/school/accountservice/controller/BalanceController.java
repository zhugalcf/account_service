package faang.school.accountservice.controller;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account/balances")
@RequiredArgsConstructor
@Slf4j
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    public BalanceDto getBalance(@RequestParam("accountId") long accountId) {
        log.info("Endpoint<getBalance>, uri = '/account/balances?accountId={}' was called successfully", accountId);
        return balanceService.getBalance(accountId);
    }

    @PostMapping
    public BalanceDto createBalance(@RequestParam("accountId") long accountId) {
        log.info("Endpoint<createBalance>, uri = '/account/balances?accountId={}' was called successfully", accountId);
        return balanceService.create(accountId);
    }

    @PutMapping
    public BalanceDto updateBalance(@Valid @RequestBody BalanceDto balanceDto) {
        log.info("Endpoint<updateBalance>, uri ='/account/balances' was called successfully");
        return balanceService.update(balanceDto);
    }
}

package faang.school.accountservice.controller;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/history/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<BalanceAuditDto> getBalanceAudit(@PathVariable long id) {
        log.info("Received request to get balance audit by id = {}", id);
        return balanceService.getBalanceAudits(id);
    }
}

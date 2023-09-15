package faang.school.accountservice.controller;

import faang.school.accountservice.dto.balance.BalanceDto;
import faang.school.accountservice.dto.balance.BalanceUpdateDto;
import faang.school.accountservice.service.balance.BalanceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService service;

    @PostMapping("/{accountId}/create")
    public BalanceDto create(@NotNull @PathVariable Long accountId) {
        return service.create(accountId);
    }

    @GetMapping("/{balanceId}/get")
    public BalanceDto get(@NotNull @PathVariable Long balanceId) {
        return service.get(balanceId);
    }

    @PostMapping("/update")
    public BalanceDto update(@Valid @RequestBody BalanceUpdateDto balanceUpdateDto) {
        return service.update(balanceUpdateDto);
    }

}

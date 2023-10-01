package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.UpdateBalanceDto;
import faang.school.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class AccountController {
    private final AccountService accountService;
    @PostMapping("/new-account/{accountNumber}")
    public AccountDto createAccount(@PathVariable Long accountNumber){
        return accountService.createAccount(accountNumber);
    }

    @GetMapping("/balance/{balanceId}")
    public BalanceDto getBalance(@PathVariable Long balanceId){
        return accountService.getBalance(balanceId);
    }

    @PutMapping("/balance/{balanceId}")
    public BalanceDto updateBalance(@Valid UpdateBalanceDto updateBalanceDto){
        return accountService.updateBalance(updateBalanceDto);
    }
}

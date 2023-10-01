package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountCreationRequest;
import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.UpdateBalanceDto;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.owner.Owner;
import faang.school.accountservice.enums.Currency;
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
    @PostMapping("/new-account")
    public AccountDto createAccount(@RequestBody AccountCreationRequest accountCreationRequest){
        return accountService.createAccount(accountCreationRequest);
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

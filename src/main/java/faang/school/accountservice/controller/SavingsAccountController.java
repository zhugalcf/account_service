package faang.school.accountservice.controller;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.service.SavingsAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/savings-account")
@RequiredArgsConstructor
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    @PostMapping
    public SavingsAccountDto open(@RequestBody @Valid SavingsAccountDto savingsAccount){
        return savingsAccountService.open(savingsAccount);
    }

    @PutMapping("/{id}")
    public SavingsAccountDto updateTariff(@PathVariable long id, @RequestParam("tariffId") long tariffId) {
        return savingsAccountService.updateTariff(id, tariffId);
    }

    @GetMapping("/{id}")
    public SavingsAccountDto getSavingAccountById(@PathVariable long id){
        return savingsAccountService.getSavingAccountById(id);
    }

    @GetMapping("/account/{accountId}")
    public SavingsAccountDto getSavingAccountByAccountId(@PathVariable long accountId){
        return savingsAccountService.getSavingAccountByAccountId(accountId);
    }
}

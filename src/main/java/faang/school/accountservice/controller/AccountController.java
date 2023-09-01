package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto openAccount(@RequestBody AccountDto accountDto) {
        return accountService.openAccount(accountDto);
    }

    @PutMapping("/freeze/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto freezeAccount(@PathVariable Long id) {
        return accountService.freezeAccount(id);
    }

    @PutMapping("/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto blockAccount(@PathVariable Long id) {
        return accountService.blockAccount(id);
    }

    @DeleteMapping("/close/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto closeAccount(@PathVariable Long id) {
        return accountService.closeAccount(id);
    }
}


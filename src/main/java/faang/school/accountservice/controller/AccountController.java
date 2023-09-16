package faang.school.accountservice.controller;

import faang.school.accountservice.dto.account.AccountDto;
import faang.school.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public AccountDto get(@PathVariable(value = "id") Long id) {
        log.info("Request to get account with id: {}", id);

        AccountDto result = accountService.get(id);

        return result;
    }

    @PostMapping("/")
    public AccountDto create(@Valid @RequestBody AccountDto accountDto) {
        log.info("Request to create account: {}", accountDto);

        AccountDto result = accountService.create(accountDto);

        return result;
    }

    @PutMapping("/freeze/{id}")
    public AccountDto freeze(@PathVariable(value = "id") Long id) {
        log.info("Request to freeze account with id: {}", id);

        AccountDto result = accountService.freeze(id);

        return result;
    }

    @PutMapping("/block/{id}")
    public AccountDto block(@PathVariable(value = "id") Long id) {
        log.info("Request to block account with id: {}", id);

        AccountDto result = accountService.block(id);

        return result;
    }

    @PutMapping("/close/{id}")
    public AccountDto close(@PathVariable(value = "id") Long id) {
        log.info("Request to close account with id: {}", id);

        AccountDto result = accountService.close(id);

        return result;
    }
}

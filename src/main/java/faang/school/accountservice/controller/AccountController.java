package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getAccount(@PathVariable long id) {
        log.info("Received request to get account by id = {}", id);
        return accountService.getAccount(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto openAccount(@RequestBody AccountDto accountDto) {
        log.info("Received request to open account by id = {}", accountDto.getId());
        return accountService.openAccount(accountDto);
    }

    @PutMapping("/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto blockAccount(@PathVariable long id) {
        log.info("Received request to block account by id = {}", id);
        return accountService.blockAccount(id);
    }

    @DeleteMapping("/close/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto closeAccount(@PathVariable long id) {
        log.info("Received request to close account by id = {}", id);
        return accountService.closeAccount(id);
    }
}
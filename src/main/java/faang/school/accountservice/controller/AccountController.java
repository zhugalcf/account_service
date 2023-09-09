package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.service.AccountService;
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

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    public AccountDto get(@PathVariable Long id) {
        log.info("Received request to get account with id {}", id);
        return accountService.get(id);
    }

    @PostMapping
    public AccountDto open(@RequestBody @Valid AccountDto accountDto) {
        log.info("Received request to open account {}", accountDto);
        return accountService.open(accountDto);
    }

    @PutMapping("/{id}/block")
    public AccountDto block(@PathVariable Long id) {
        log.info("Received request to block account with id {}", id);
        return accountService.block(id);
    }

    @PutMapping("/{id}/close")
    public AccountDto close(@PathVariable Long id) {
        log.info("Received request to close account with id {}", id);
        return accountService.close(id);
    }
}

package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Account", description = "Account operations")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    @Operation(summary = "Get account")
    public AccountDto get(@PathVariable Long id) {
        log.info("Received request to get account with id {}", id);
        return accountService.get(id);
    }

    @PostMapping
    @Operation(summary = "Create account")
    public AccountDto open(@RequestBody @Valid AccountDto accountDto) {
        log.info("Received request to open account {}", accountDto);
        return accountService.open(accountDto);
    }

    @PutMapping("/{id}/block")
    @Operation(summary = "Block account")
    public AccountDto block(@PathVariable Long id) {
        log.info("Received request to block account with id {}", id);
        return accountService.block(id);
    }

    @PutMapping("/{id}/close")
    @Operation(summary = "Close account")
    public AccountDto close(@PathVariable Long id) {
        log.info("Received request to close account with id {}", id);
        return accountService.close(id);
    }
}

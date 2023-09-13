package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountRequestDto;
import faang.school.accountservice.dto.AccountResponseDto;
import faang.school.accountservice.service.AccountService;
import jakarta.validation.Valid;
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
    public AccountResponseDto getAccount(@PathVariable long id) {
        log.info("Received request to get account by id = {}", id);
        return accountService.getAccount(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto openAccount(@RequestBody @Valid AccountRequestDto accountDto) {
        log.info("Your account was created at: {}", accountDto.getCreatedAt());
        return accountService.openAccount(accountDto);
    }

    @PutMapping("/block/{id}")
    public AccountResponseDto blockAccount(@PathVariable long id) {
        log.info("Received request to block account by id = {}", id);
        return accountService.blockAccount(id);
    }

    @DeleteMapping("/close/{id}")
    public AccountResponseDto closeAccount(@PathVariable long id) {
        log.info("Received request to close account by id = {}", id);
        return accountService.closeAccount(id);
    }

    @PutMapping("/unlock/{id}")
    public AccountResponseDto unlockAccount(@PathVariable long id) {
        log.info("Received request to unlock account by id = {}", id);
        return accountService.unlockAccount(id);
    }
}
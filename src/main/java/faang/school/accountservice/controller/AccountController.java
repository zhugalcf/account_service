package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private AccountService accountService;

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long accountId) {
        AccountDto accountDto = accountService.getAccount(accountId);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/open")
    public ResponseEntity<AccountDto> openAccount(@RequestBody AccountRequestDto accountRequest) {
        AccountDto newAccount = accountService.openAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }

    @PutMapping("/block/{accountId}")
    public ResponseEntity<Void> blockAccount(@PathVariable Long accountId) {
        accountService.blockAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/close/{accountId}")
    public ResponseEntity<Void> closeAccount(@PathVariable Long accountId) {
        accountService.closeAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}


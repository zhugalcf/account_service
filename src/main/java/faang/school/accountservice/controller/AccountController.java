package faang.school.accountservice.controller;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.dto.ReplenishmentRequest;
import faang.school.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }


    @PostMapping("/open")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto open(@RequestBody AccountDto accountDto) {
        return accountService.open(accountDto);
    }

    @PostMapping("/block/{id}")
    public ResponseEntity<String> block(@PathVariable Long id) {
        accountService.block(id);
        return new ResponseEntity<>("Account successfully blocked", HttpStatus.OK);
    }

    @PostMapping("/close/{id}")
    public ResponseEntity<String> close(@PathVariable Long id) {
        accountService.close(id);
        return new ResponseEntity<>("Account successfully closed", HttpStatus.OK);
    }

    @PostMapping("/deposit/{id}")
    public ResponseEntity<String> deposit(@PathVariable Long id, @RequestBody @Validated ReplenishmentRequest dto) {
        accountService.deposit(id, dto);
        return new ResponseEntity<>("Account has been successfully replenished for the amount " + dto.sum().toString(), HttpStatus.OK);

    }
}

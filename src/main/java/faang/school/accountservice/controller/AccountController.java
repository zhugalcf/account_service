package faang.school.accountservice.controller;

import faang.school.accountservice.dto.account.CreateAccountDto;
import faang.school.accountservice.dto.account.ResponseAccountDto;
import faang.school.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseAccountDto open(@RequestBody CreateAccountDto createAccountDto) {
        return accountService.open(createAccountDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseAccountDto get(@PathVariable long id) {
        return accountService.get(id);
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<Void> block(@PathVariable long id) {
        accountService.block(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<Void> close(@PathVariable long id) {
        accountService.close(id);
        return ResponseEntity.ok().build();
    }


}

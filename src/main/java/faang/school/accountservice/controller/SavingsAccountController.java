package faang.school.accountservice.controller;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.dto.SavingsAccountResponseDto;
import faang.school.accountservice.service.SavingsAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/savings/account")
@Slf4j
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    @PostMapping("/open")
    public SavingsAccountResponseDto openSavingsAccount(@RequestBody @Valid SavingsAccountDto savingsAccountDto) {
        log.info("Received request to open account for savings by account id = {}", savingsAccountDto.getAccountId());
        return savingsAccountService.openSavingsAccount(savingsAccountDto);
    }

    @GetMapping("/{id}")
    public SavingsAccountResponseDto getSavingsAccountByAccountId(@PathVariable long id) {
        log.info("Received request of getting savings account with account id: {}", id);
        return savingsAccountService.getSavingsAccountByAccountId(id);
    }
//
//    @GetMapping("/user/{ownerId}")
//    public SavingsAccountResponseDto getSavingsAccountByOwnerId(@PathVariable long ownerId) {
//        log.info("Received request of getting savings account with ownerId: {}", ownerId);
//        return savingsAccountService.getSavingsAccountByOwnerId(ownerId);
//    }
}

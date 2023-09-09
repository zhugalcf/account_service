package faang.school.accountservice.controller;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.service.SavingsAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/savings/account")
@Slf4j
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;

    @PostMapping("/")
    public SavingsAccountDto openSavingsAccount(@RequestBody @Valid SavingsAccountDto savingsAccountDto) {
        log.info("Received request to open account for savings by account id = {}", savingsAccountDto.getAccountId());
        return savingsAccountService.openSavingsAccount(savingsAccountDto);
    }

    @GetMapping("/{id}")
    public void getSavingsAccountBy(long id) {
        savingsAccountService.getSavingsAccountBy(id);
    }
}

package faang.school.accountservice.controller;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@Validated
@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
@Tag(name = "Balance", description = "Balance operations")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{accountId}")
    @Operation(summary = "Get balance by account id")
    public BalanceDto getBalance(@PathVariable Long accountId) {
        return balanceService.getBalanceByAccountId(accountId);
    }

    @PostMapping("/{accountId}")
    @Operation(summary = "Create balance")
    public BalanceDto createBalance(@PathVariable Long accountId, @Valid @RequestBody BalanceDto balanceDto) {
        return balanceService.createBalance(accountId, balanceDto);
    }

    @PutMapping("/{accountId}")
    @Operation(summary = "Update balance")
    public BalanceDto updateBalance(@PathVariable Long accountId, @Valid @RequestBody BalanceDto balanceDto) {
        return balanceService.updateBalance(accountId, balanceDto);
    }

    @PostMapping("/deposit")
    @Operation(summary = "Deposit")
    public BalanceDto deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        return balanceService.deposit(accountId, amount);
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw")
    public BalanceDto withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        return balanceService.withdraw(accountId, amount);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer")
    public ResponseEntity<Void> transfer(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam BigDecimal amount) {
        balanceService.transfer(senderId, receiverId, amount);
        return ResponseEntity.ok().build();
    }
}

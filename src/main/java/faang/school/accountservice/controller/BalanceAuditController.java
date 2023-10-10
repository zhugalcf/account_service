package faang.school.accountservice.controller;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.service.BalanceAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/balances/audit")
@RequiredArgsConstructor
@Slf4j
public class BalanceAuditController {
    private final BalanceAuditService balanceAuditService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceAuditDto getBalanceAudit(@PathVariable long id) {
        log.info("Received request to get balance audit by id = {}", id);
        return balanceAuditService.getBalanceAudits(id);
    }
}

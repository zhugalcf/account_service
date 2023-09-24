package faang.school.accountservice.controller;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.service.BalanceAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/balance/audit")
public class BalanceAuditController {
    private final BalanceAuditService balanceAuditService;

    @GetMapping("/{accountId}")
    List<BalanceAuditDto> getBalanceAudits(@PathVariable long accountId) {
        return balanceAuditService.getBalanceAudits(accountId);
    }
}
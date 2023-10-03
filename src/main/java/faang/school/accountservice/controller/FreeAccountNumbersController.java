package faang.school.accountservice.controller;

import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.service.FreeAccountNumbersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/numbers")
@RequiredArgsConstructor
public class FreeAccountNumbersController {
    private final FreeAccountNumbersService freeAccountNumbersService;

    @PostMapping("/generate")
    public void createFreeAccountNumbers(@RequestParam int count,
                                         @RequestParam AccountType accountType) {
        freeAccountNumbersService.generateAccountNumbersOfType(count, accountType);
    }

    @PostMapping("/generate/reach")
    public void createFreeAccountNumbersToReach(@RequestParam int count,
                                                @RequestParam AccountType accountType) {
        freeAccountNumbersService.generateAccountNumbersToReach(count, accountType);
    }
}

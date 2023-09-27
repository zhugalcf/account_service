package faang.school.accountservice.controller;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.service.FreeAccountNumbersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/number")
@RequiredArgsConstructor
public class AccountNumberController {

    private final FreeAccountNumbersService service;

    @GetMapping
    public ResponseEntity<Object> createFreeNumber(@RequestParam("type") String typeString) {
        AccountType type = AccountType.valueOf(typeString);
        return ResponseEntity.ok(service.getNumber(type));
    }
}

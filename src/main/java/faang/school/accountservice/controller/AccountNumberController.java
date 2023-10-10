package faang.school.accountservice.controller;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.service.FreeAccountNumbersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/number")
@RequiredArgsConstructor
@Slf4j
public class AccountNumberController {

    private final FreeAccountNumbersService service;

    @PostMapping
    public ResponseEntity<Object> createFreeNumber(@RequestParam("type") AccountType type) {
        return ResponseEntity.ok(service.getNumber(type, (t, s) -> log.info("Number {} of type {} was accepted.", s, t)));
    }
}

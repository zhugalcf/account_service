//package faang.school.accountservice.controller;
//
//import faang.school.accountservice.dto.SavingsAccountDto;
//import faang.school.accountservice.service.SavingsAccountService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@Validated
//@RequestMapping("/savings-account")
//public class SavingsAccountController {
//    private final SavingsAccountService savingsAccountService;
//
//    @PostMapping("/open")
//    public SavingsAccountDto openSavingsAccount(@RequestParam Long ownerId, @RequestParam String currencyCode) {
//        SavingsAccountDto accountDto = savingsAccountService.openSavingsAccount(ownerId, currencyCode);
//        log.info("Savings account opened successfully with id {}", accountDto.getId());
//        return accountDto;
//    }
//
//    @GetMapping("/{id}")
//    public SavingsAccountDto getSavingsAccountById(@PathVariable Long id) {
//        log.info("Savings account retrieved successfully for id {}", id);
//        return savingsAccountService.getSavingsAccountById(id);
//    }
//
//    @GetMapping("/info/{id}")
//    public SavingsAccountDto getSavingsAccountInfoById(@PathVariable Long id) {
//        log.info("Tariff and rate details of savings account retrieved successfully for id {}", id);
//        return savingsAccountService.getSavingsAccountInfoById(id);
//    }
//}
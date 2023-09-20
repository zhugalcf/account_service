package faang.school.accountservice.controller;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.AccountNumber;
import faang.school.accountservice.service.FreeAccountNumbersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/number")
@RequiredArgsConstructor
public class AccountNumberController {

    private final FreeAccountNumbersService service;

//    @PostMapping("/new")
//    public AccountNumber createNewNumber(@RequestParam("type") String type){
//        AccountType accountNumberType = AccountType.valueOf(type);
//        return service.createNewNumber(accountNumberType);
//    }

    @GetMapping
    public AccountType returnType(){
        return AccountType.DEBIT;
    }

    @PostMapping
    public ResponseEntity<Object> createSequence(@RequestParam("type") String typeString){
        AccountType type = AccountType.valueOf(typeString);
        service.createNewSequence(type);
        return ResponseEntity.ok(type);
    }

}

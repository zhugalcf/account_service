package faang.school.accountservice.service;

import faang.school.accountservice.model.AccountNumber;
import faang.school.accountservice.model.AccountNumberType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("number")
@RequiredArgsConstructor
public class AccountNumberController {

    private final FreeAccountNumbersService service;

    @PostMapping("/new")
    public AccountNumber createNewNumber(AccountNumberType type){
        return service.createNewNumber(type);
    }
}

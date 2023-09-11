package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.enums.TariffType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SavingsAccountServiceTest {


    @Autowired
    private SavingsAccountService savingsAccountService;

    @Test
    void someTest() {
        SavingsAccountDto savingsAccountDto = SavingsAccountDto.builder()
                .accountId(1)
                .tariffType(TariffType.BASIC)
                .build();

        savingsAccountService.openSavingsAccount(savingsAccountDto);
    }
}
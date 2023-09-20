package faang.school.accountservice.service;

import faang.school.accountservice.entity.FreeAccountNumber;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.repository.FreeAccountNumberRepository;
import faang.school.accountservice.util.BaseContextTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FreeAccountNumbersServiceTest extends BaseContextTest {
    @Autowired
    FreeAccountNumbersService freeAccountNumbersService;
    @Autowired
    FreeAccountNumberRepository freeAccountNumberRepository;

    @Test
    public void generateAccountNumberTest() {
        freeAccountNumbersService.generateAccountNumber(AccountType.PAYMENT_ACCOUNT);
        Optional<FreeAccountNumber> freeAccountNumber = freeAccountNumberRepository.findByAccountType(AccountType.PAYMENT_ACCOUNT);
        assertTrue(freeAccountNumber.isPresent());
    }
}
package faang.school.accountservice.service;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.account.number.AccountNumber;
import faang.school.accountservice.repository.AccountNumberContainer;
import faang.school.accountservice.repository.AccountNumbersSequenceRepository;
import faang.school.accountservice.repository.FreeAccountNumbersRepository;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FreeAccountNumbersServiceTest {

    @Autowired
    private FreeAccountNumbersRepository numbersRepository;
    @Autowired
    private AccountNumbersSequenceRepository sequenceRepository;
    @Autowired
    private FreeAccountNumbersService service;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = AccountNumberContainer.getInstance();
    @BeforeAll
    public void setUp() {
    }

    @Test
    void testGetNumber() {
        String number = service.getNumber(AccountType.DEBIT, (t, s) -> {
        });
        assertNotNull(number);
        assertEquals(20, number.length());
        assertTrue(number.startsWith("4276"));
        assertFalse(numbersRepository.exists(Example.of(AccountNumber.builder()
                .account_number(number)
                .build())));
    }

    @Test
    void testGetNumberWithLambda() {
        AccountNumber expected = AccountNumber.builder()
                .type(AccountType.CREDIT)
                .build();
        AccountNumber actual = new AccountNumber();
        String num = service.getNumber(AccountType.CREDIT, (type, accountNumber) -> {
            actual.setType(type);
            actual.setAccount_number(accountNumber);
        });
        expected.setAccount_number(num);
        assertEquals(expected, actual);
    }

    @Test
    void testGetAndIncrementSequence() {
        long expected = sequenceRepository.findByType(AccountType.CREDIT.toString()) + 1;
        long actual = service.getAndIncrementSequence(AccountType.CREDIT);
        assertEquals(expected, actual);
    }

    @Test
    public void testOptimisticLock() {
        long current = sequenceRepository.findByType(AccountType.CREDIT.toString());
        sequenceRepository.increment(AccountType.CREDIT.toString(), current);
        sequenceRepository.increment(AccountType.CREDIT.toString(), current);
        assertEquals(++current, sequenceRepository.findByType(AccountType.CREDIT.toString()));

    }
}
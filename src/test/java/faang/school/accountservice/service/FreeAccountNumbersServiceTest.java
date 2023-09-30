package faang.school.accountservice.service;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.AccountNumber;
import faang.school.accountservice.model.AccountNumbersSequence;
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

import static org.junit.jupiter.api.Assertions.*;

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
        numbersRepository.deleteAll();
        sequenceRepository.deleteAll();
        sequenceRepository.save(AccountNumbersSequence.builder()
                .type(AccountType.CREDIT)
                .current(3)
                .version(0)
                .build());
    }

    @Test
    void testGetNumber() {
        String number = service.getNumber(AccountType.DEBIT);
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
        long actual = service.getAndIncrementSequence(AccountType.CREDIT);
        assertEquals(4, actual);
    }
}
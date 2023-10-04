package faang.school.accountservice.repository;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.account.number.AccountNumbersSequence;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class AccountNumbersSequenceRepositoryTest {

//    @Autowired
//    private AccountNumbersSequenceRepository repository;
//
//    @ClassRule
//    public static PostgreSQLContainer postgreSQLContainer = AccountNumberContainer.getInstance();
//
//    @BeforeAll
//    public void setUp() {
//        repository.deleteAll();
//        repository.save(AccountNumbersSequence.builder()
//                .type(AccountType.SAVINGS)
//                .current(1)
//                .build());
//        repository.save(AccountNumbersSequence.builder()
//                .type(AccountType.CURRENCY)
//                .current(2)
//                .build());
//        repository.save(AccountNumbersSequence.builder()
//                .type(AccountType.CREDIT)
//                .current(3)
//                .build());
//    }
//
//    @Test
//    void findByType() {
//        long current = repository.findByType(AccountType.CURRENCY.toString());
//        assertEquals(2, current);
//    }
//
//    @Test
//    void createNewCounterIfNotExists() {
//        repository.createNewCounterIfNotExists(AccountType.DEBIT.toString());
//        repository.findByType(AccountType.DEBIT.toString());
//        repository.createNewCounterIfNotExists(AccountType.DEBIT.toString());
//        repository.findByType(AccountType.DEBIT.toString());
//    }
//
//    @Test
//    void increment() {
//        long current = repository.findByType(AccountType.SAVINGS.toString());
//        repository.increment(AccountType.SAVINGS.toString(), current);
//        current = repository.findByType(AccountType.SAVINGS.toString());
//        assertEquals(2, current);
//    }
}
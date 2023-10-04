package faang.school.accountservice.repository;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.account.number.AccountNumber;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class FreeAccountNumbersRepositoryTest {

//    @Autowired
//    private FreeAccountNumbersRepository repository;
//
//    @ClassRule
//    public static PostgreSQLContainer postgreSQLContainer = AccountNumberContainer.getInstance();
//
//    @BeforeAll
//    public void setUp(){
//        repository.deleteAll();
//    }
//
//    @Test
//    void createNewNumber() {
//        repository.createNewNumber(AccountType.DEBIT.toString(), "1024");
//    }
//
//    @Test
//    void findFreeNumber() {
//        AccountNumber number = repository.findFreeNumber(AccountType.DEBIT.toString());
//        assertEquals("1024", number.getAccount_number());
//    }
//
//    @Test
//    void deleteFreeNumber() {
//
//    }
//
//    @Test
//    void countAccountNumberByType() {
//    }
}
package faang.school.accountservice.repository;

import faang.school.accountservice.model.AccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FreeAccountNumbersRepository extends JpaRepository<AccountNumber, String> {

    default void createNewNumber(AccountNumber accountNumber){
        save(accountNumber);
    }

    @Query(value = "BEGIN TRANSACTION;" +
            "DELETE FROM AccountNumber an LIMIT 1 RETERNING an;" +
            "COMMIT;")
    AccountNumber getFreeNumber();
}

package faang.school.accountservice.repository;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.AccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FreeAccountNumbersRepository extends JpaRepository<AccountNumber, Long> {
    @Query(nativeQuery = true,
            value = """
                    INSERT INTO free_account_numbers (type, account_number)
                            SELECT :type, :number
                            WHERE NOT EXISTS 
                            (SELECT * FROM free_account_numbers WHERE type = :type AND account_number = :number)
                    """)
    void createNewNumber(@Param("type") AccountType type, @Param("number") String number);

    @Query(nativeQuery = true,
            value = """
                    DELETE FROM free_account_numbers 
                    WHERE id = (SELECT id FROM free_account_numbers WHERE type = :type LIMIT 1)
                    RETERNING account_number
                    """)
    @Transactional
    String getFreeNumber(@Param("type") AccountType type);

    long countAccountNumberByType(AccountType type);
}
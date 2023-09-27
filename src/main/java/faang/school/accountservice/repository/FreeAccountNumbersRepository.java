package faang.school.accountservice.repository;

import faang.school.accountservice.model.AccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeAccountNumbersRepository extends JpaRepository<AccountNumber, Long> {

    @Query(nativeQuery = true,
            value = """
                    INSERT INTO free_account_numbers (type, account_number)
                            SELECT :type, :number
                            WHERE NOT EXISTS 
                            (SELECT * FROM free_account_numbers WHERE type = :type AND account_number = :number)
                    """)
    @Modifying
    void createNewNumber(@Param("type") String type, @Param("number") String number);

    @Query(nativeQuery = true,
            value = """
                    DELETE FROM free_account_numbers 
                    WHERE id = :id
                    """)
    @Modifying
    int deleteFreeNumber(@Param("id") long id);

    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM free_account_numbers WHERE type = :type LIMIT 1
                                        """)
    AccountNumber findFreeNumber(@Param("type") String type);

    @Query(nativeQuery = true,
            value = """
                    SELECT COUNT(*) FROM free_account_numbers WHERE type = :type
                    """)
    long countAccountNumberByType(@Param("type") String type);
}

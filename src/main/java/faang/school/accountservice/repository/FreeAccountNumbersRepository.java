package faang.school.accountservice.repository;

import faang.school.accountservice.model.account.number.AccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface FreeAccountNumbersRepository extends JpaRepository<AccountNumber, Long> {

    @Query(nativeQuery = true,
            value = """
                    INSERT INTO free_account_numbers (type, account_number)
                    VALUES(:type, :number)
                    ON CONFLICT(type, account_number) DO NOTHING
                    """)
    @Modifying
    @Transactional
    void createNewNumber(@Param("type") String type, @Param("number") String number);

    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM free_account_numbers WHERE type = :type
                    ORDER BY (type, account_number)
                    LIMIT 1
                                        """)
    AccountNumber findFreeNumber(@Param("type") String type);

    @Query(nativeQuery = true,
            value = """
                    SELECT COUNT(*) FROM free_account_numbers WHERE type = :type
                    """)
    long countAccountNumberByType(@Param("type") String type);
}

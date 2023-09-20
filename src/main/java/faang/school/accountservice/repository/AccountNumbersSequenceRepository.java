package faang.school.accountservice.repository;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.AccountNumbersSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumbersSequence, Long> {

    @Query(nativeQuery = true,
            value = """
                    INSERT INTO account_numbers_sequence (type, current, version)
                    SELECT :type, 0, 0
                    WHERE NOT EXISTS (SELECT * FROM account_numbers_sequence WHERE type = :type)
                    """)
    void createNewCounter(@Param("type") AccountType type);

    @Query(nativeQuery = true,
            value = """
            UPDATE account_numbers_sequence SET current = current + 1 WHERE type  = :type
            RETURNING true
            """)
    @Transactional
    boolean increment(@Param("type") AccountType type);

    AccountNumbersSequence getAccountNumbersSequenceByType(AccountType type);
}

package faang.school.accountservice.repository;

import faang.school.accountservice.model.account.number.AccountNumbersSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumbersSequence, Long> {

    @Query(nativeQuery = true,
            value = """
                    INSERT INTO account_numbers_sequence (type, current)
                    VALUES(:type, 0)
                    ON CONFLICT(type) DO NOTHING
                    """)
    @Modifying
    @Transactional
    void createNewCounterIfNotExists(@Param("type") String type);

    @Query(nativeQuery = true,
            value = """
                    UPDATE account_numbers_sequence SET current = current + 1 
                    WHERE type  = :type AND current = :current
                    """)
    @Modifying
    @Transactional
    int increment(@Param("type") String type, @Param("current") long current);

    @Query(nativeQuery = true,
            value = """
                    SELECT current FROM account_numbers_sequence WHERE type = :type
                    """)
    long findByType(@Param("type") String type);
}

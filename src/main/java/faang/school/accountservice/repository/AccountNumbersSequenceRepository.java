package faang.school.accountservice.repository;

import faang.school.accountservice.model.AccountNumbersSequence;
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
                    INSERT INTO account_numbers_sequence (type, current, version)
                    SELECT :type, 0, 0
                    WHERE NOT EXISTS (SELECT * FROM account_numbers_sequence WHERE type = :type)
                    """)
    @Modifying
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

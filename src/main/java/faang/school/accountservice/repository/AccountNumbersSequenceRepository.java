package faang.school.accountservice.repository;

import faang.school.accountservice.entity.account.numbers.AccountNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumberSequence, Long> {
    @Query(nativeQuery = true, value = """
            INSERT INTO account_number_sequence (account_type, current_count)
            VALUES (:accountType, 0)
            """)
    @Modifying
    void createAccountNumberSequence(@Param("accountType") int accountType);

    @Query(nativeQuery = true, value = """
            SELECT current_count 
            FROM account_number_sequence 
            WHERE account_type = :accountType
            """)
    Optional<Long> getCurrentCountByAccountType(@Param("accountType") int accountType);

    @Query(nativeQuery = true, value = """
            UPDATE account_number_sequence
            SET current_count = current_count + 1
            WHERE account_type = :accountType
            """)
    @Modifying
    void incrementByAccountType(@Param("accountType") int accountType);
}
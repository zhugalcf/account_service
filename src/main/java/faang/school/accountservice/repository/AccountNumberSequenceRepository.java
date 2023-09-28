package faang.school.accountservice.repository;

import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.model.AccountNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountNumberSequenceRepository extends JpaRepository<AccountNumberSequence, Long> {

    Optional<AccountNumberSequence> findByAccountType(AccountType accountType);

    @Query(nativeQuery = true, value = """
            UPDATE account_numbers_sequence a 
            SET current_count = a.current_count + 1
            WHERE a.account_type = :accountType AND a.current_count = :currentCount
            RETURNING a.current_count
            """)
    Optional<Long> incrementCurrentCount(int accountType, Long currentCount);
}

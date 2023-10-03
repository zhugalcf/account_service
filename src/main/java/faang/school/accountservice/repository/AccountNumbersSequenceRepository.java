package faang.school.accountservice.repository;

import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.numbers.AccountNumberSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumberSequence, Long> {

    Optional<Long> findByAccountType(AccountType accountType);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "INSERT INTO account_number_sequence (account_type, current_count)" +
                    "VALUES (:accountType, 0)")
    Long createAccountNumberSequence(@Param("accountType") AccountType accountType);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE account_number_sequence ans " +
                    "SET current_count = ans.current_count + 1 " +
                    "WHERE ans.account_type = :accountType")
    boolean incrementByAccountType(@Param("accountType") AccountType accountType);
}
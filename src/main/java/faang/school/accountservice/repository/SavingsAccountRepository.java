package faang.school.accountservice.repository;

import faang.school.accountservice.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    @Query(value = "select tariff_history from savings_account t where t.id = ?1 and t.account_id = ?2",
            nativeQuery = true)
    Optional<String> tariffHistory(long scoreId, long userId);
}


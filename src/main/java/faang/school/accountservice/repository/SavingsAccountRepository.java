package faang.school.accountservice.repository;

import faang.school.accountservice.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    @Query(value = "select tariff_history from savings_account t where t.id = ?1 and t.account_id = ?2",
            nativeQuery = true)
    Optional<String> tariffHistory(long scoreId, long userId);

    @Query(value = "select s.id from savings_account s inner join account a on s.account_id = a.id where a.status = 0",
            nativeQuery = true)
    List<Long> activeAccountsId();
}



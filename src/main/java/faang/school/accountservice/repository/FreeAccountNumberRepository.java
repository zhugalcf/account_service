package faang.school.accountservice.repository;

import faang.school.accountservice.entity.FreeAccountNumber;
import faang.school.accountservice.entity.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeAccountNumberRepository extends JpaRepository<FreeAccountNumber, Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM free_account_numbers WHERE account_type = :accountType ORDER BY account_number DESC LIMIT 1")
    FreeAccountNumber findFirstByOrderByAccountNumber(AccountType accountType);

    void deleteFirstByAccountNumber(String accountNumber);

}

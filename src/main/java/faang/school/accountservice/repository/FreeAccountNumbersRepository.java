package faang.school.accountservice.repository;

import faang.school.accountservice.model.AccountNumber;
import faang.school.accountservice.model.AccountNumberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FreeAccountNumbersRepository extends JpaRepository<AccountNumber, String> {

    @Query(nativeQuery = true,
            value = "DELETE FROM free_account_numbers WHERE type = :type LIMIT 1 RETERNING account_number")
    @Transactional
    String getFreeNumber(@Param("type") AccountNumberType type);
}

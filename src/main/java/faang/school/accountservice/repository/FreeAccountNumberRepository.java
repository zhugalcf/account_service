package faang.school.accountservice.repository;

import faang.school.accountservice.model.AccountType;
import faang.school.accountservice.model.FreeAccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreeAccountNumberRepository extends JpaRepository<FreeAccountNumber, Long> {

    Optional<FreeAccountNumber> findByAccountType(AccountType accountType);
}

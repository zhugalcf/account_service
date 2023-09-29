package faang.school.accountservice.repository;

import faang.school.accountservice.entity.account.numbers.FreeAccountNumber;
import faang.school.accountservice.entity.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreeAccountNumbersRepository extends JpaRepository<FreeAccountNumber, Long> {

    long countByAccountType(AccountType accountType);

    Optional<FreeAccountNumber> findFirstByAccountTypeOrderByCreatedAtAsc(AccountType accountType);
}
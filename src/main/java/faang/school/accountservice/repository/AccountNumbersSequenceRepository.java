package faang.school.accountservice.repository;

import faang.school.accountservice.entity.AccountNumbersSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumbersSequence, Long> {
    Optional<AccountNumbersSequence> findByAccountType(String accountType);

}
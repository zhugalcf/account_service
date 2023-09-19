package faang.school.accountservice.repository;

import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.numbers.AccountNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountNumberSequenceRepository extends JpaRepository<AccountNumberSequence, Long> {

    AccountNumberSequence findByAccountType(AccountType accountType);
}
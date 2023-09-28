package faang.school.accountservice.repository;

import faang.school.accountservice.model.Balance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends CrudRepository<Balance, Long> {
}

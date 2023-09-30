package faang.school.accountservice.repository;

import faang.school.accountservice.entity.balance.Balance;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Optional<Balance> findByAccountId(@NotNull Long accountId);

    @Query("SELECT b FROM Balance b WHERE b.account.id IN :accountIds")
    List<Balance> findAllByAccountIds(List<Long> accountIds);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Balance b WHERE b.account.id = :accountId")
    boolean existsByAccountId(@NotNull Long accountId);
}
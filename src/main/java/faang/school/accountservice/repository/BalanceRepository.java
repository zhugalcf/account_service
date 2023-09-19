package faang.school.accountservice.repository;

import faang.school.accountservice.entity.balance.Balance;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    @Query("SELECT b FROM Balance b WHERE b.account.id = :accountId")
    Optional<Balance> findByAccountId(@NotNull Long accountId);
}
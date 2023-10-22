package faang.school.accountservice.repository;

import faang.school.accountservice.model.Account;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.owner_id = :ownerId")
    Optional<Account> findByOwnerId(@Param("ownerId") long ownerId);
}

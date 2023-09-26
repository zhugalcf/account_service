package faang.school.accountservice.repository;

import faang.school.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM account
            WHERE owner_id = :ownerId
            AND account_type = 'SAVINGS'
            AND owner_type = :ownerType
            """)
    Optional<Account> findAccountByOwnerIdAndOwnerType(long ownerId, String ownerType);
}

package faang.school.accountservice.repository;

import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    @Query("SELECT sa FROM SavingsAccount sa WHERE sa.account.id = :accountId AND sa.account.owner.id = :ownerId")
    SavingsAccount findByIdAndOwnerId(Long accountId, Long ownerId);

    List<SavingsAccount> findAllByAccountStatus(AccountStatus accountStatus);

    SavingsAccount findByAccount_Id(Long accountId);

    @Query("SELECT sa.id FROM SavingsAccount sa")
    List<Long> findAllSavingsAccountIds();
}
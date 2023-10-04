package faang.school.accountservice.repository;

import faang.school.accountservice.entity.SavingsAccountTariffHistory;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountTariffHistoryRepository extends JpaRepository<SavingsAccountTariffHistory, Long> {
    SavingsAccountTariffHistory findTopBySavingsAccountOrderByChangeDateDesc(SavingsAccount savingsAccount);


}
package faang.school.accountservice.repository;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.SavingsAccountTariffHistory;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountTariffHistoryRepository extends JpaRepository<SavingsAccountTariffHistory, Long> {
    SavingsAccountTariffHistory findTopBySavingsAccountOrderByChangeDateDesc(SavingsAccount savingsAccount);

    @Query("SELECT t FROM SavingsAccountTariffHistory h " +
            "JOIN h.tariff t " +
            "WHERE h.savingsAccount.id = :clientId " +
            "ORDER BY h.changeDate DESC")
    TariffDto getCurrentTariffAndRateByClientId(@Param("clientId") Long clientId);
}
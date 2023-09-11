package faang.school.accountservice.repository;

import faang.school.accountservice.model.TariffHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffHistoryRepository extends JpaRepository<TariffHistory, Long> {
}

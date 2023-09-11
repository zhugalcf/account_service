package faang.school.accountservice.repository;

import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {

    Optional<Tariff> getByType(TariffType tariffType);
}

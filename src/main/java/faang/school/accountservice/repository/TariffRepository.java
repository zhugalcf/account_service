package faang.school.accountservice.repository;

import faang.school.accountservice.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
    @Query(value = "select * from tariff where type_tariff = ?",
            nativeQuery = true)
    Tariff findByType(String type);
}

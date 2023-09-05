package faang.school.accountservice.repository;

import faang.school.accountservice.model.FreeAccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeAccountNumberRepository extends JpaRepository<FreeAccountNumber, Long> {
}

package faang.school.accountservice.repository;

import faang.school.accountservice.model.FreeAccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeAccountNumberRepository extends JpaRepository<FreeAccountNumber, FreeAccountNumber.FreeAccountNumberKey> {
}

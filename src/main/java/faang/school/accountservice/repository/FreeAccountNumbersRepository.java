package faang.school.accountservice.repository;

import faang.school.accountservice.entity.FreeAccountNumbers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeAccountNumbersRepository extends JpaRepository<FreeAccountNumbers, Long> {



}

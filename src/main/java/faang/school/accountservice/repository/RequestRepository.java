package faang.school.accountservice.repository;

import faang.school.accountservice.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    Optional<Request> findByUserIdAndLockAndOpenIsTrue(Long lock, Long userId);

    @Query(nativeQuery = true, value = """
        SELECT * FROM request LIMIT 10
    """)
    List<Request> findAllWithLimit();
}

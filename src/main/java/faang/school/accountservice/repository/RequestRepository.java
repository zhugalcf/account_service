package faang.school.accountservice.repository;

import faang.school.accountservice.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query(nativeQuery = true, value = """
        SELECT * FROM request WHERE uuid = :requestId FOR UPDATE
    """)
    Optional<Request> findRequestsByRequestIdForUpdate(UUID requestId);
}

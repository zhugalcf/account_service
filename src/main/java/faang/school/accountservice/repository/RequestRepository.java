package faang.school.accountservice.repository;

import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request,Long> {

    List<Request> findByOwnerIdAndOwnerType(long ownerId, OwnerType ownerType);
    Optional<Request> findByIdempotencyKey(UUID idempotencyKey);

    @Query("SELECT r FROM Request r\n" +
            "WHERE r.lockValue = :lockValue\n" +
            "AND r.isOpen = true")
    Optional <Request> findByLockValue(String lockValue);
}

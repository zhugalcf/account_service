package faang.school.accountservice.repository;

import faang.school.accountservice.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findByOwnerId(long ownerId);
    Optional<Request> findByIdempotencyKey(UUID idempotencyKey);

    List<Request> findByIsOpen();
}

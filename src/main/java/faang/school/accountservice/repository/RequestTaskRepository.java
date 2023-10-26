package faang.school.accountservice.repository;

import faang.school.accountservice.model.RequestTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestTaskRepository extends JpaRepository<RequestTask, Long> {
    Optional<List<RequestTask>> findAllByRequest_IdempotentToken(UUID requestId);
}

package faang.school.accountservice.repository;

import faang.school.accountservice.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findByOwnerId(long ownerId);
}

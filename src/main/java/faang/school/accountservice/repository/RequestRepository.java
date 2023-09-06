package faang.school.accountservice.repository;

import faang.school.accountservice.entity.request.Request;
import faang.school.accountservice.entity.request.RequestStatus;
import faang.school.accountservice.entity.request.RequestType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    List<Request> findByUserId(@NotNull Long userId);
    List<Request> findByRequestStatus(@NotNull RequestStatus requestStatus);
    List<Request> findByRequestType(@NotNull RequestType requestType);
}
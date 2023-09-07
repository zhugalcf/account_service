package faang.school.accountservice.repository;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByUserId(Long userId);

    @Query("select r from Request r where r.lock = ?1 and r.userId = ?2 and r.isOpen = true")
    Optional<Request> findByLockAndUserIdAndIsOpenTrue(String lock, Long userId);

    Optional<Request> findByIdempotentToken(String idempotentToken);

    @Query(nativeQuery = true, value = """
            SELECT * FROM requests WHERE idempotent_token = ?1 FOR UPDATE
            """)
    Optional<Request> findByIdempotentTokenForUpdate(String idempotentToken);

    @Query(nativeQuery = true, value = """
            SELECT * FROM requests WHERE status = ?1 FOR UPDATE
            """)
    List<Request> findAllByStatusForUpdate(RequestStatus status);
}

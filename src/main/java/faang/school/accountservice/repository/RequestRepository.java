package faang.school.accountservice.repository;

import faang.school.accountservice.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByUserId(Long userId);

    @Query("select r from Request r where r.lock = ?1 and r.userId = ?2 and r.isOpen = true")
    Request findByLockAndUserIdAndIsOpenTrue(String lock, Long userId);
}

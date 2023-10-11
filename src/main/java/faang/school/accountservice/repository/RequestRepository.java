package faang.school.accountservice.repository;

import faang.school.accountservice.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query(nativeQuery = true, value = """
        SELECT * FROM request WHERE lock IS NOT NULL AND is_open IS TRUE
         AND status = 1 LIMIT 10
    """)
    List<Request> findAllWithLimit();
}

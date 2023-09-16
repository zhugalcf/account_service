package faang.school.accountservice.repository;

import faang.school.accountservice.model.Request;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Map;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {

    @Override
    Request save(Request request) throws DataIntegrityViolationException;

    Request findByUserIdAndLockValue(Long userId, Long lockValue);

    Request findByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT user_id, COUNT(*) as result_count FROM request" +
            "WHERE (created_at > :time OR updated_at > :time) GROUP BY user_id HAVING COUNT(*) > :maxNumberOfRequests")
    Map<Long, Long> findAllGroupedByUserIdForPeriod(ZonedDateTime time, Long maxNumberOfRequests);
}

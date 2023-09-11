package faang.school.accountservice.repository;

import faang.school.accountservice.model.AccountNumberType;
import faang.school.accountservice.model.AccountNumbersSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumbersSequence, AccountNumberType> {

    @Query("INSERT INTO AccountNumbersSequence (type, current) VALUES (:type, 0) " +
            "WHERE NOT EXISTS (SELECT * FROM account_numbers_sequence WHERE type = :type);")
    void createNewCounter(@Param("type") AccountNumberType type);

    @Query("IF EXISTS(SELECT * FROM account_numbers_sequence WHERE type = :type) " +
            "THEN" +
            "UPDATE AccountNumbersSequence SET current = current + 1 WHERE type  = :type " +
            "RETURN true " +
            "ELSE " +
            "RETURN false;")
    @Transactional
    boolean optimisticIncrement(@Param("type") AccountNumberType type);
}

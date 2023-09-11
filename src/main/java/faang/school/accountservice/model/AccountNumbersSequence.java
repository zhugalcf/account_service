package faang.school.accountservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_numbers_sequence")
public class AccountNumbersSequence {
    @Id
    @Column(name = "type", length = 20, nullable = false, unique = true)
    private AccountNumberType type;
    @Version
    @Column(name = "current", nullable = false)
    private long current;
}

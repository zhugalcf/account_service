package faang.school.accountservice.entity.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "currency")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    @Column(name = "code", nullable = false, unique = true, length = 3)
    private String code;
}

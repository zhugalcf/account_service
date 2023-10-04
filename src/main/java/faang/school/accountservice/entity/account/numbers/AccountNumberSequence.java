package faang.school.accountservice.entity.account.numbers;

import faang.school.accountservice.entity.account.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account_number_sequence")
public class AccountNumberSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "current_count", nullable = false)
    private Long currentCount;

    @Version
    private Long version;
}
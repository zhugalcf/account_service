package faang.school.accountservice.entity.account.numbers;

import faang.school.accountservice.entity.account.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "free_account_numbers")
public class FreeAccountNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime createdAt;
}
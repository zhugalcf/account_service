package faang.school.accountservice.entity;

import faang.school.accountservice.entity.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="balance")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "number", referencedColumnName = "number", unique = true)
    private Account account;

    @Column(name="authorization_balance", nullable = false)
    private BigDecimal authorizationBalance;

    @Column(name="current_balance", nullable = false)
    private BigDecimal currentBalance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime updated;

    @Version
    @Column(name="balance_version", nullable = false)
    private Long balanceVersion;
}

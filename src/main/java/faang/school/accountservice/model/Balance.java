package faang.school.accountservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

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
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", unique = true)
    private Account account;

    @Column(name="authorization_balance", nullable = false)
    private BigDecimal authorizationBalance;

    @Column(name="current_balance", nullable = false)
    private BigDecimal currentBalance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private ZonedDateTime created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private ZonedDateTime updated;

    @Column(name="balance_version", nullable = false)
    private Long balanceVersion;
}

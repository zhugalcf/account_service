package faang.school.accountservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "balance_audit", indexes = {
        @Index(name = "idx_account_id", columnList = "account_id")
})
public class BalanceAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance_version")
    private Long balanceVersion;

    @Column(name = "authorization_balance")
    private BigDecimal authorizationBalance;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @Column(name = "operation_id")
    private Long operationId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Override
    public String toString() {
        return "BalanceAudit{" +
                "id=" + id +
                ", balanceVersion=" + balanceVersion +
                ", authorizationBalance=" + authorizationBalance +
                ", currentBalance=" + currentBalance +
                ", operationId=" + operationId +
                ", createdAt=" + createdAt +
                ", account=" + (account != null ? account.getId() : null) +
                '}';
    }
}
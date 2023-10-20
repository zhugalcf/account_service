package faang.school.accountservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "balance_audit")
public class BalanceAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "balance_id")
    private Balance balance;

    @Version
    @Column(name = "balance_audit_version", nullable = false)
    private long version;

    @Column(name = "authorization_amount")
    private BigDecimal authorizationAmount;

    @Column(name = "actual_amount")
    private BigDecimal actualAmount;

    @Column(name = "operation_id")
    private long operationId;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "audit_timestamp", nullable = false)
    private LocalDateTime auditTimestamp;
}

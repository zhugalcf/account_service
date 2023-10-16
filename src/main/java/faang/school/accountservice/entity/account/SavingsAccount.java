package faang.school.accountservice.entity.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "savings_account")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavingsAccount {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Account account;

    @ElementCollection
    @CollectionTable(name = "savings_account_tariff_history", joinColumns = @JoinColumn(name = "savings_account_id"))
    private List<Long> tariffHistoryIds;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;

    @Column(name = "savings_account_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatus accountStatus;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_calculation_at")
    private LocalDateTime lastUpdateCalculationAt;

    @Version
    @Column(name = "version")
    private Long version;
}
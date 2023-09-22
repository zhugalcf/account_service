package faang.school.accountservice.entity;

import faang.school.accountservice.entity.account.SavingsAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "savings_account_tariff_history")
public class SavingsAccountTariffHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "savings_account_id", nullable = false)
    private SavingsAccount savingsAccount;

    @ManyToOne
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    @Column(name = "change_date", nullable = false)
    private LocalDateTime changeDate;
}
package faang.school.accountservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@Table(name = "tariff")
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private TypeTariff type;

    @Column(name = "rate", nullable = false)
    private double rate;

    @Column(name = "rate_history")
    private String rateHistory;

    @OneToMany(mappedBy = "tariff", cascade = CascadeType.ALL)
    private List<SavingsAccount> savingsAccounts;
}

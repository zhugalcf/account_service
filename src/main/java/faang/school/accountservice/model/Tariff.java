package faang.school.accountservice.model;

import faang.school.accountservice.enums.TariffType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tariff")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = true)
    private TariffType type;

    @OneToMany(mappedBy = "tariff", fetch = FetchType.LAZY)
    private List<TariffHistory> tariffHistory;

    @OneToMany(mappedBy = "tariff", fetch = FetchType.LAZY)
    private List<Rate> rateHistory;

}

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "saving_account_id")
//    private SavingsAccount savingsAccount;

//    @OneToMany(mappedBy = "tariff", fetch = FetchType.LAZY)
//    private List<SavingsAccount> savingsAccounts;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "saving_account_id")
//    private List<SavingsAccount> savingsAccounts;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "rate_id")
//    private Rate rate;
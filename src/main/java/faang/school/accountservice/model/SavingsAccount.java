package faang.school.accountservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "savings_account")
public class SavingsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "savings_account_number", length = 20)
    private String accountNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "savingsAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TariffHistory> tariffHistory;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_interest_date")
    private LocalDateTime lastInterestCalculateDate;

    @Version
    @Column(name = "version", nullable = false)
    private int version;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SavingsAccount that = (SavingsAccount) o;

        if (id != that.id) return false;
        if (version != that.version) return false;
        if (!Objects.equals(accountNumber, that.accountNumber))
            return false;
        if (!Objects.equals(account, that.account)) return false;
        if (!Objects.equals(balance, that.balance)) return false;
        if (!Objects.equals(tariffHistory, that.tariffHistory))
            return false;
        if (!Objects.equals(lastInterestCalculateDate, that.lastInterestCalculateDate))
            return false;
        if (!Objects.equals(createdAt, that.createdAt)) return false;
        return Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (tariffHistory != null ? tariffHistory.hashCode() : 0);
        result = 31 * result + (lastInterestCalculateDate != null ? lastInterestCalculateDate.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", account=" + account +
                ", balance=" + balance +
                ", tariffHistory=" + tariffHistory +
                ", lastInterestCalculateDate=" + lastInterestCalculateDate +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
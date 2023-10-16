package faang.school.accountservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tariff_history")
public class TariffHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "savings_account_id")
    private SavingsAccount savingsAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TariffHistory that = (TariffHistory) o;

        if (id != that.id) return false;
        if (!Objects.equals(savingsAccount, that.savingsAccount))
            return false;
        if (!Objects.equals(tariff, that.tariff)) return false;
        return Objects.equals(lastModifiedDate, that.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (savingsAccount != null ? savingsAccount.hashCode() : 0);
        result = 31 * result + (tariff != null ? tariff.hashCode() : 0);
        result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TariffHistory{" +
                "id=" + id +
                ", savingsAccount=" + savingsAccount +
                ", tariff=" + tariff +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}

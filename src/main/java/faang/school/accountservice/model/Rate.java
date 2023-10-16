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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "rate")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "rate_percent", nullable = false)
    private float percent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rate rate = (Rate) o;

        if (id != rate.id) return false;
        if (Float.compare(rate.percent, percent) != 0) return false;
        return Objects.equals(tariff, rate.tariff);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (percent != +0.0f ? Float.floatToIntBits(percent) : 0);
        result = 31 * result + (tariff != null ? tariff.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", percent=" + percent +
                ", tariff=" + tariff +
                '}';
    }
}

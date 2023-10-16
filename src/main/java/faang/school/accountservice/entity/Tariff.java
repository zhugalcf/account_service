package faang.school.accountservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tariff")
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tariff_type", nullable = false, length = 64)
    @Enumerated(EnumType.ORDINAL)
    private TariffType tariffType;

    @ElementCollection
    @CollectionTable(name = "tariff_rates", joinColumns = @JoinColumn(name = "tariff_id"))
    private List<BigDecimal> rates;
}
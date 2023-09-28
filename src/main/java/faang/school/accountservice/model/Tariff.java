package faang.school.accountservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tariff")
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type_tariff", nullable = false, length = 64, unique = true)
    private String typeTariff;

    @Column(name = "bet", nullable = false)
    private float bet;

    @Column(name = "betting_history")
    private String bettingHistory;
}

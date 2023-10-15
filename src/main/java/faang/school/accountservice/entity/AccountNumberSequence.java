package faang.school.accountservice.entity;

import faang.school.accountservice.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_number_sequence")
public class AccountNumberSequence {

    @Id
    @Column(name = "type", nullable = false, length = 32)
    private AccountType type;

    @Column(name = "counter", nullable = false)
    private long counter;

    @Transient
    private long initialValue;
}

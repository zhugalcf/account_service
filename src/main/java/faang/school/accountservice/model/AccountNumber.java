package faang.school.accountservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "free_account_numbers")
public class AccountNumber {

    @Id
    @Column(name = "account_number", length = 20, nullable = false, unique = true)
    private String account_number;
    @Column(name = "type", length = 20, nullable = false)
    private AccountNumberType type;

}

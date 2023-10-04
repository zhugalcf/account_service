package faang.school.accountservice.model.account.number;

import faang.school.accountservice.enums.AccountType;
import jakarta.persistence.*;
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
@IdClass(AccountNumberId.class)
public class AccountNumber {

    @Id
    @Column(name = "account_number", length = 20, nullable = false)
    private String account_number;
    @Id
    @Column(name = "type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;

}

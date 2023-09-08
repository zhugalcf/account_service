package faang.school.accountservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "free_account_numbers")
public class AccountNumber {

    @Id
    private String account_number;
    @Column(name = "type", length = 20, nullable = false)
    private AccountNumberType type;

}

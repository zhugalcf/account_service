package faang.school.accountservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "free_account_numbers")
public class AccountNumber {

    @Id
    @GeneratedValue()
    private Long id;
    private String type;
    private Long account_number;
}

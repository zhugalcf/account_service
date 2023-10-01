package faang.school.accountservice.dto;

import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private Long id;
    private BigInteger number;
    private Long owner;
    private AccountType type;
    private Currency currency;
    private AccountStatus status;
}

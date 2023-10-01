package faang.school.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BalanceDto {
    private Long id;
    private BigInteger accountNumber;
    private BigDecimal authorizationBalance;
    private BigDecimal currentBalance;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long balanceVersion;
}

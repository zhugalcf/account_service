package faang.school.accountservice.dto.balance;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class BalanceHistoryDto {
    private Long id;
    private Long balanceId;
    private BalanceUpdateType type;
    private BigDecimal amount;
    private Instant createdAt;
}

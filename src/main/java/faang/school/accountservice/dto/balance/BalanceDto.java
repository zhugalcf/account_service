package faang.school.accountservice.dto.balance;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class BalanceDto {

    @NotNull
    private Long id;
    @NotNull
    private Long accountId;
    private BigDecimal authBalance;
    private BigDecimal actualBalance;
    private Instant createdAt;
    private Instant updatedAt;
}

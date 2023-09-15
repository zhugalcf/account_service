package faang.school.accountservice.dto.balance;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BalanceUpdateDto(@NotNull Long balanceId,
                               @NotNull BalanceUpdateType type,
                               @NotNull BigDecimal amount) {

}

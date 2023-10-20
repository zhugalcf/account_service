package faang.school.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceAuditDto {
    private long id;
    private long version;
    private BigDecimal authorizationAmount;
    private BigDecimal actualAmount;
    private long operationId;
    private LocalDateTime auditTimestamp;
}

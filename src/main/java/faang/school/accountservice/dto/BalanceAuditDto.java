package faang.school.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceAuditDto {
    private Long id;
    private Long balanceVersion;
    private BigDecimal authorizationBalance;
    private BigDecimal currentBalance;
    private Long operationId;
    private ZonedDateTime createdAt;
    private Long accountId;
}
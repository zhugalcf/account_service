package faang.school.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceDto {
    private Long id;
    private Long accountId;
    private BigDecimal authorizationBalance;
    private BigDecimal currentBalance;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
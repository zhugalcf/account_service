package faang.school.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.accountservice.entity.request.RequestStatus;
import faang.school.accountservice.entity.request.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingOperationDto {

    private UUID id;

    private RequestType operationType;

    private RequestStatus operationStatus;

    private BigDecimal amount;

    private Long userId;

    private Long senderAccountId;

    private Long receiverAccountId;

    private String currency;

    private Instant scheduledAt;

    private Instant createdAt;

    private Instant updatedAt;
}

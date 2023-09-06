package faang.school.accountservice.dto;

import faang.school.accountservice.model.request.RequestStatus;
import faang.school.accountservice.model.request.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private Long id;
    private UUID idempotencyKey;
    private Long ownerId;
    private RequestType requestType;
    private String lockValue;
    private boolean isOpen;
    private Map<String, Object> inputData;
    private RequestStatus requestStatus;
    private String statusDetails;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
}

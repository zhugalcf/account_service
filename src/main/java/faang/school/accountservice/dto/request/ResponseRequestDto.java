package faang.school.accountservice.dto.request;

import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRequestDto {
    private Long id;
    private String idempotentToken;
    private Long userId;
    private RequestType type;
    private String lock;
    private boolean isOpen;
    private Map<String, Object> input;
    private RequestStatus status;
    private String details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}

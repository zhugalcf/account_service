package faang.school.accountservice.dto.request;

import faang.school.accountservice.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestStatusChangedEvent {
    private String idempotentToken;
    private Long userId;
    private RequestStatus status;
}

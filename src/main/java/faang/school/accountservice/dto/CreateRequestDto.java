package faang.school.accountservice.dto;

import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRequestDto {
    private String idempotentToken;
    private Long userId;
    private RequestType type;
    private String lock;
    private Map<String, Object> input;
    private RequestStatus status;
    private String details;
}

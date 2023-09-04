package faang.school.accountservice.dto;

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
    private String uuid;
    private Long userId;
    private RequestType requestType;
    private String lockValue;
    private boolean isOpen;
    private Map<String, Object> inputData;
    private RequestStatus requestStatus;
    private String statusDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}

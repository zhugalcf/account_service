package faang.school.accountservice.dto;

import faang.school.accountservice.model.request.RequestStatus;
import faang.school.accountservice.model.request.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private Long id;
    private String uuid;
    private Long userId;
    private RequestType requestType;
    private String lockValue;
    private boolean isOpen;
    private String inputData;
    private RequestStatus requestStatus;
    private String statusDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}

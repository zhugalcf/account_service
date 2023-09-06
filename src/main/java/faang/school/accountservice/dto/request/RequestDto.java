package faang.school.accountservice.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.accountservice.entity.request.RequestStatus;
import faang.school.accountservice.entity.request.RequestType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDto implements Serializable {
    private UUID id;
    @NotNull(message = "User id is required")
    private Integer userId;
    @NotNull(message = "Request type is required")
    private RequestType requestType;
    private Long lockValue;
    private Boolean isOpen;
    private Map<String, Object> inputData;
    private RequestStatus requestStatus;
    private String statusDetails;
    private Instant createdAt;
    private Instant lastModified;
    @Min(value = 1, message = "Version must be at least 1")
    private Integer version;
}
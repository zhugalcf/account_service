package faang.school.accountservice.dto.request;

import faang.school.accountservice.enums.RequestType;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class RequestDto {
    private UUID requestId;
    private Long userId;
    @Null
    private RequestType requestType;
    private String input;
    private String additionally;
    @Null
    private LocalDateTime createdAt;
    @Null
    private LocalDateTime updatedAt;
}

package faang.school.accountservice.dto;

import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.mapper.JsonConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class RequestDto {
    @NotNull
    private Long userId;
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> inputData;
    @NotNull
    private RequestStatus requestStatus;
    private Long lockValue;
    private String additionalData;
}

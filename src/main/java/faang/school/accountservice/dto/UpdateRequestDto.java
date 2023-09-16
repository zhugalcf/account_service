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
public class UpdateRequestDto {
    @NotNull
    private Long userId;
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> inputData;
    private RequestStatus requestStatus;
    @NotNull
    private Long lockValue;
    private String additionalData;
}

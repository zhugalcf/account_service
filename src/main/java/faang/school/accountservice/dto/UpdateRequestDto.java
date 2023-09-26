package faang.school.accountservice.dto;

import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.mapper.JsonConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UpdateRequestDto {
    @Id
    private long id;
    private Long userId;
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> inputData;
    private RequestStatus requestStatus;
    private String additionalData;
}

package faang.school.accountservice.dto.request;

import faang.school.accountservice.enums.RequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
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
    @NotBlank
    private String idempotentToken;
    @Null
    private Long userId;
    @NotNull
    private RequestType type;
    @NotBlank
    private String lock;
    @NotNull
    private Map<String, Object> input;
    @NotNull
    private String details;
}

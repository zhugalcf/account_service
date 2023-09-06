package faang.school.accountservice.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class UpdateRequestDto {
    @NotBlank
    private String idempotentToken;
    @NotBlank
    private String lock;
    @Null
    private Long userId;
    private String details;
    private Map<String, Object> input;
    private Boolean cancel;
}

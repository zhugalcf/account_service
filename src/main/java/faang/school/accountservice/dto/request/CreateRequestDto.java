package faang.school.accountservice.dto.request;

import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.model.request.RequestType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestDto {

    @NotBlank
    private UUID idempotencyKey;
    @NotBlank
    private Long ownerId;
    @NotBlank
    private OwnerType ownerType;
    @NotBlank
    private RequestType requestType;
    private String lockValue;
    @NotBlank
    private Object inputData;
}

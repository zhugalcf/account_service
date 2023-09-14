package faang.school.accountservice.dto;

import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class RequestDto {
    @NotNull
    private Long requestId;

    @NotNull
    private Long userId;

    @NotNull
    private String lockValue;

    private boolean active;

    @NotBlank
    private String details;

    @NotNull
    private Long version;

    private RequestType requestType;

    private RequestStatus requestStatus;
}

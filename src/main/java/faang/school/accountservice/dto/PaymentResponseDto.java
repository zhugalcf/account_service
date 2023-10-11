package faang.school.accountservice.dto;

import faang.school.accountservice.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    @NotNull(message = "UUID can't be empty")
    private UUID idempotencyKey;
    @NotNull(message = "Status in response of payment can't be null")
    private PaymentStatus status;
}

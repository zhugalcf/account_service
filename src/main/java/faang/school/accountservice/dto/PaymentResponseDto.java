package faang.school.accountservice.dto;

import faang.school.accountservice.enums.PaymentStatus;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private UUID idempotencyKey;
    @NotNull
    private PaymentStatus paymentStatus;
}

package faang.school.accountservice.dto;

import faang.school.accountservice.enums.Currency;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    @NotNull
    @Size(min = 12, max = 20, message = "Length of receiver's number must be more than 12 and less 20")
    private String receiverAccountNumber;
    @NotNull
    @Size(min = 12, max = 20, message = "Length of owner's number must be more than 12 and less 20")
    private String ownerAccountNumber;
    @NotNull(message = "Idempotency key can't be empty")
    private UUID idempotencyKey;
    @NotNull
    private Currency currency;
    @NotNull
    private BigDecimal amount;
}

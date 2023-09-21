package faang.school.accountservice.dto;

import faang.school.accountservice.enums.TariffType;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingsAccountDto {
    private long id;
    @Min(value = 1, message = "Значение должно быть не меньше 1")
    private long accountId;
    private TariffType tariffType;
    private int version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

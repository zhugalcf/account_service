package faang.school.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingsAccountResponseDto {
    private long id;
    private long accountId;
    private TariffDto tariffDto;
    private int version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

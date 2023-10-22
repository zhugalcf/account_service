package faang.school.accountservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOpenEventDto {
    @Positive
    @NotNull
    private long ownerId;
    @NotNull
    @Size(min = 12, max = 20)
    private String number;
}

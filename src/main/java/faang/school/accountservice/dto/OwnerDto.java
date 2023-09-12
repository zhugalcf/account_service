package faang.school.accountservice.dto;

import faang.school.accountservice.enums.OwnerType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDto {
    @Positive
    @NotNull
    private long ownerId;
    @NotNull
    private OwnerType type;
}

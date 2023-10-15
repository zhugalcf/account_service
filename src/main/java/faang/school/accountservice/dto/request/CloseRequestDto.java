package faang.school.accountservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CloseRequestDto {
    private UUID requestId;
    private int status;
}

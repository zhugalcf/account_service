package faang.school.accountservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateRequestDto {
    private UUID requestId;
    private Long userId;
    private String input;
    private String additionally;
}

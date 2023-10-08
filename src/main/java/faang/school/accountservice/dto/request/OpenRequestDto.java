package faang.school.accountservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class OpenRequestDto {
    private UUID requestId;
    private Long lock;
}

package faang.school.accountservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecuteRequestEvent {
    private String idempotentToken;
    private String userId;
}

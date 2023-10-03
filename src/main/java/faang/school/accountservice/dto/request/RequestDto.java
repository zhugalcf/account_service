package faang.school.accountservice.dto.request;

import faang.school.accountservice.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RequestDto {
    private UUID requestId;
    private Long userId;
    private RequestType requestType;
    private String input;
    private String additionally;
}

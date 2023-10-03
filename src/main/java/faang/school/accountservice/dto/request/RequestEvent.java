package faang.school.accountservice.dto.request;

import faang.school.accountservice.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestEvent {
    private Long userId;
    private RequestType requestType;
    private String input;
    private String additionally;
}

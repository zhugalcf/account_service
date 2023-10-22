package faang.school.accountservice.handlers;

import faang.school.accountservice.dto.AccountOpenEventDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;
import faang.school.accountservice.publisher.AccountPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SendNotificationHandler implements RequestTaskHandler<String, Object> {
    private final AccountPublisher accountPublisher;

    @Override
    public Map<String, Object> execute(Request request, Map<String, Object> context) {
        Long ownerId = (Long) context.get("ownerId");
        String number = (String) context.get("number");
        accountPublisher.publishMessage(new AccountOpenEventDto(ownerId, number));
        return context;
    }

    @Override
    public RequestHandler getHandlerId() {
        return RequestHandler.SEND_NOTIFICATION;
    }
}
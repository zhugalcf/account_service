package faang.school.accountservice.handlers;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateCashbackRecordHandler implements RequestTaskHandler<String, Object> {
    @Override
    public Map<String, Object> execute(Request request, Map<String, Object> context) {
        //задача не реализована
        return context;
    }

    @Override
    public RequestHandler getHandlerId() {
        return RequestHandler.CREATE_CASHBACK_RECORD;
    }
}

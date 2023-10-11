package faang.school.accountservice.interfaces;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;

public interface RequestTaskHandler {
    void execute(Request request);

    RequestHandler getHandlerId();
}

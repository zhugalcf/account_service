package faang.school.accountservice.handlers;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;

import java.util.Map;

public interface RequestTaskHandler<K, V> {
    Map<K, V> execute(Request request, Map<K, V> context);

    RequestHandler getHandlerId();
}

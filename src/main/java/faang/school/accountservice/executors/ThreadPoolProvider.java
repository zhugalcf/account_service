package faang.school.accountservice.executors;


import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;
import faang.school.accountservice.enums.RequestType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public interface ThreadPoolProvider {
    ThreadPoolTaskExecutor getThreadPool();

    RequestType getType();
}

package faang.school.accountservice.executors;

import faang.school.accountservice.enums.RequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
@Configuration
@ConfigurationProperties(prefix = "account-executor")
public class OpenAccountProvider implements ThreadPoolProvider{
    private int uniqueBatch;
    private int threadPoolSize;
    private int threadQueue;
    @Override
    public ThreadPoolTaskExecutor getThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(threadPoolSize);
        executor.setQueueCapacity(threadQueue);
        executor.initialize();
        return executor;
    }

    @Override
    public RequestType getType() {
        return RequestType.OPEN_ACCOUNT;
    }
}

package faang.school.accountservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Value("${thread-pools.exceed.core}")
    private int exceedCoreSize;
    @Value("${thread-pools.exceed.max}")
    private int maxExceedCoreSize;
    @Value("${thread-pools.exceed.alive-time}")
    private int aliveTimeExceed;
    @Value("${thread-pools.exceed.queue}")
    private int exceedQueue;
    @Value("${thread-pools.create.core}")
    private int createCoreSize;
    @Value("${thread-pools.create.max}")
    private int maxCreateCoreSize;
    @Value("${thread-pools.create.alive-time}")
    private int aliveTimeCreate;
    @Value("${thread-pools.create.queue}")
    private int createQueue;


    @Bean
    public ExecutorService requestsPool(){
        return new ThreadPoolExecutor(exceedCoreSize, maxExceedCoreSize, aliveTimeExceed, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(exceedQueue));
    }

    @Bean
    public ExecutorService createRequestsPool(){
        return new ThreadPoolExecutor(createCoreSize, maxCreateCoreSize, aliveTimeCreate, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(createQueue));
    }
}


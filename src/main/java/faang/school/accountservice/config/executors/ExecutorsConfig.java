package faang.school.accountservice.config.executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorsConfig {
    @Value("${accrual.executors-pool}")
    private int pool;

    @Bean
    public ExecutorService executor() {
        return Executors.newFixedThreadPool(100);
    }

    @Bean
    public ExecutorService accrualExecutor() {
        return Executors.newFixedThreadPool(pool);
    }

    @Bean
    public ExecutorService requestPool() {
        return Executors.newFixedThreadPool(2);
    }
}

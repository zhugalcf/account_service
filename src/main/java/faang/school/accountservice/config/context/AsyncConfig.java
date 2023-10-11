package faang.school.accountservice.config.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("numberGenerator")
    public Executor numberGenerator() {
        return Executors.newSingleThreadExecutor();
    }

}

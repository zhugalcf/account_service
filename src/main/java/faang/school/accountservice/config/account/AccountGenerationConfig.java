package faang.school.accountservice.config.account;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.account.generation")
public class AccountGenerationConfig {
    private long numberOfAccounts;
    private int accountNumberLength;
    private int scheduleIntervalInSeconds;
}

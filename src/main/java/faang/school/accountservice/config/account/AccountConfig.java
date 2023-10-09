package faang.school.accountservice.config.account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "account-generation")
@Data
public class AccountConfig {
        private int amountToGenerate;
        private int retryDelay;
}

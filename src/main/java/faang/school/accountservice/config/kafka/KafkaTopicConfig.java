package faang.school.accountservice.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topics.account-create}")
    private String accountCreatedTopic;

    @Value("${spring.kafka.topics.account-freeze}")
    private String accountFrozenTopic;

    @Value("${spring.kafka.topics.account-close}")
    private String accountClosedTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic accountCreatingTopic() {
        return new NewTopic(accountCreatedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic accountFreezingTopic() {
        return new NewTopic(accountFrozenTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic accountClosedTopic() {
        return new NewTopic(accountClosedTopic, 1, (short) 1);
    }
}


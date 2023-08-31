package faang.school.accountservice.config.kafka;

import faang.school.accountservice.dto.account.AccountClosingEvent;
import faang.school.accountservice.dto.account.AccountCreationEvent;
import faang.school.accountservice.dto.account.AccountFreezingEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, AccountCreationEvent> producerFactoryAccountCreationEvent() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ProducerFactory<String, AccountFreezingEvent> producerFactoryAccountFreezingEvent() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ProducerFactory<String, AccountClosingEvent> producerFactoryAccountClosingEvent() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, AccountCreationEvent> kafkaTemplateForAccountCreation() {
        return new KafkaTemplate<>(producerFactoryAccountCreationEvent());
    }

    @Bean
    public KafkaTemplate<String, AccountFreezingEvent> kafkaTemplateForAccountFreezing() {
        return new KafkaTemplate<>(producerFactoryAccountFreezingEvent());
    }

    @Bean
    public KafkaTemplate<String, AccountClosingEvent> kafkaTemplateForAccountClosing() {
        return new KafkaTemplate<>(producerFactoryAccountClosingEvent());
    }
}

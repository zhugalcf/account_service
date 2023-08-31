package faang.school.accountservice.messaging;

import faang.school.accountservice.dto.account.AccountCreationEvent;
import faang.school.accountservice.messaging.account.AccountCreationEventPublisher;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.AccountStatus;
import faang.school.accountservice.model.AccountType;
import faang.school.accountservice.model.Currency;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        topics = {"account-create"},
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        })
class AccountCreationEventPublisherIT {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private AccountCreationEventPublisher accountCreationEventPublisher;

    @Value("${spring.kafka.topics.account-create}")
    private String TOPIC;

    @Test
    void send_ShouldSendToBroker() {
        Consumer<String, AccountCreationEvent> consumerServiceTest = createConsumer(TOPIC);

        accountCreationEventPublisher.send(mockAccount());

        ConsumerRecord<String, AccountCreationEvent> record =
                KafkaTestUtils.getSingleRecord(consumerServiceTest, TOPIC);

        AccountCreationEvent received = record.value();

        Assertions.assertEquals(mockAccountCreationEvent(), received);
    }

    private Consumer<String, AccountCreationEvent> createConsumer(final String topicName) {
        Map<String, Object> consumerProps =
                KafkaTestUtils.consumerProps("group_consumer_test", "false", embeddedKafkaBroker);

        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Consumer<String, AccountCreationEvent> consumer =
                new DefaultKafkaConsumerFactory<>(consumerProps,
                        new StringDeserializer(),
                        new JsonDeserializer<>(AccountCreationEvent.class, false))
                        .createConsumer();

        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topicName);

        return consumer;
    }

    private AccountCreationEvent mockAccountCreationEvent() {
        return AccountCreationEvent.builder()
                .userId(1L)
                .number("1234534654733564")
                .build();
    }

    private Account mockAccount() {
        return Account.builder()
                .id(1L)
                .number("1234534654733564")
                .userId(1L)
                .type(AccountType.CURRENT_ACCOUNT)
                .currency(Currency.USD)
                .status(AccountStatus.ACTIVE)
                .version(1L)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

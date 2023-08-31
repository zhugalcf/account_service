package faang.school.accountservice.messaging.account;

import faang.school.accountservice.dto.account.AccountCreationEvent;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.messaging.EventPublisher;
import faang.school.accountservice.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountCreationEventPublisher implements EventPublisher<Account> {

    private final KafkaTemplate<String, AccountCreationEvent> kafkaTemplateForAccountCreation;
    private final AccountMapper accountMapper;

    @Value("${spring.kafka.topics.account-create}")
    private String accountCreationTopic;

    @Override
    public Class<?> getEventType() {
        return AccountCreationEvent.class;
    }

    @Override
    public void send(Account account) {
        AccountCreationEvent event = accountMapper.toAccountCreateEvent(account);

        CompletableFuture<SendResult<String, AccountCreationEvent>> future = kafkaTemplateForAccountCreation
                .send(accountCreationTopic, event);

        future.whenComplete((result, e) -> {
            if (e == null) {
                log.info("Account creation event was sent: {}", result);
            } else {
                log.error("Failed to send account creation event: {}", e.getMessage());
            }
        });
    }
}

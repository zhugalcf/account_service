package faang.school.accountservice.messaging.account;

import faang.school.accountservice.dto.account.AccountFreezingEvent;
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
public class AccountFrozenEventPublisher implements EventPublisher<Account> {

    private final KafkaTemplate<String, AccountFreezingEvent> kafkaTemplateForAccountFreezing;
    private final AccountMapper accountMapper;

    @Value("${spring.kafka.topics.account-freeze}")
    private String accountFreezingTopic;

    @Override
    public Class<?> getEventType() {
        return AccountFreezingEvent.class;
    }

    @Override
    public void send(Account account) {
        AccountFreezingEvent event = accountMapper.toAccountFreezingEvent(account);

        CompletableFuture<SendResult<String, AccountFreezingEvent>> future = kafkaTemplateForAccountFreezing
                .send(accountFreezingTopic, event);

        future.whenComplete((result, e) -> {
            if (e == null) {
                log.info("Account freezing event was sent: {}", result);
            } else {
                log.error("Failed to send account freezing event: {}", e.getMessage());
            }
        });
    }
}

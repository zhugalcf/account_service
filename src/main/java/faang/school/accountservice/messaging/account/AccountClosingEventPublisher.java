package faang.school.accountservice.messaging.account;

import faang.school.accountservice.dto.account.AccountClosingEvent;
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
public class AccountClosingEventPublisher implements EventPublisher<Account> {

    private final KafkaTemplate<String, AccountClosingEvent> kafkaTemplateForAccountClosing;
    private final AccountMapper accountMapper;

    @Value("${spring.kafka.topics.account-close}")
    private String accountClosingTopic;

    @Override
    public Class<?> getEventType() {
        return AccountClosingEvent.class;
    }

    @Override
    public void send(Account account) {
        AccountClosingEvent event = accountMapper.toAccountClosingEvent(account);

        CompletableFuture<SendResult<String, AccountClosingEvent>> future = kafkaTemplateForAccountClosing
                .send(accountClosingTopic, event);

        future.whenComplete((result, e) -> {
            if (e == null) {
                log.info("Account closing event was sent: {}", result);
            } else {
                log.error("Failed to send account closing event: {}", e.getMessage());
            }
        });
    }
}

package faang.school.accountservice.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.AccountOpenEventDto;
import faang.school.accountservice.dto.account.CreateAccountDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountPublisher extends EventPublisher<AccountOpenEventDto>{
    @Value("${spring.data.redis.channels.account_channel.name}")
    private String channel;

    public AccountPublisher(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

    @Override
    protected String getChannel() {
        return channel;
    }
}

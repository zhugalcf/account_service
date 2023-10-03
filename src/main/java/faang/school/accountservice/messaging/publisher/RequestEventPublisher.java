package faang.school.accountservice.messaging.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.request.RequestEvent;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.mapper.RequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RequestMapper requestMapper;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.channel.request}")
    private String requestChannelName;

    public void publish(Request request) {
        RequestEvent requestEvent = requestMapper.toEvent(request);
        try {
            String json = objectMapper.writeValueAsString(requestEvent);
            redisTemplate.convertAndSend(requestChannelName, json);
            log.info("A message has been sent to channel {}, message: {}", requestChannelName, json);
        } catch (JsonProcessingException e) {
            log.error("An exception was thrown when reading a message in RequestEventPublisher: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

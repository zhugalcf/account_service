package faang.school.accountservice.message.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.ExecuteRequestDto;
import faang.school.accountservice.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecuteRequestPublisher implements MessagePublisher<ExecuteRequestDto>{
    private final ObjectMapper objectMapper;
    @Value("${spring.data.redis.channels.execute-request}")
    private String topic;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Async(value = "createRequestsPool")
    public void publish(ExecuteRequestDto message) {
        String json;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error during mapping RequestDto into Json");
        }
        redisTemplate.convertAndSend(topic, json);
    }
}

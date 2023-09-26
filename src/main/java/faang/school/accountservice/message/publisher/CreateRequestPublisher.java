package faang.school.accountservice.message.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.dto.UpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateRequestPublisher implements MessagePublisher<RequestDto> {
    private final ObjectMapper objectMapper;
    @Value("${spring.data.redis.channels.create-request}")
    private String topic;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Async(value = "createRequestsPool")
    public void publish(RequestDto message) {
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

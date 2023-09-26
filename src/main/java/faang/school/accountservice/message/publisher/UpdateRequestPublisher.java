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
@RequiredArgsConstructor
@Slf4j
public class UpdateRequestPublisher implements MessagePublisher<UpdateRequestDto>{
    private final ObjectMapper objectMapper;
    @Value("${spring.data.redis.channels.update-request}")
    private String topic;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Async(value = "createRequestsPool")
    public void publish(UpdateRequestDto message) {
        String json;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error during mapping updateRequestDto into Json");
        }
        redisTemplate.convertAndSend(topic, json);
    }
}

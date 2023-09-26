package faang.school.accountservice.message.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RequestListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RequestService requestService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RequestDto requestDto;
        try {
            requestDto = objectMapper.readValue(message.getBody(), RequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        requestService.createRequest(requestDto);
    }
}

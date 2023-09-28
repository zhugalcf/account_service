package faang.school.accountservice.message.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PaymentListener implements MessageListener {
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RequestDto requestDto;
        try {
            requestDto = objectMapper.readValue(message.getBody(), RequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

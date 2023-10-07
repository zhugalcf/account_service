package faang.school.accountservice.message.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.PaymentStatus;
import faang.school.accountservice.dto.RedisPaymentDto;
import faang.school.accountservice.service.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PaymentListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final PaymentProcessingService paymentProcessingService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisPaymentDto redisPaymentDto;
        try {
            redisPaymentDto = objectMapper.readValue(message.getBody(), RedisPaymentDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (redisPaymentDto.status().equals(PaymentStatus.PENDING)) {
            paymentProcessingService.createRequestForPayment(redisPaymentDto);
        } else if (redisPaymentDto.status().equals(PaymentStatus.CANCELED)) {
            paymentProcessingService.cancelRequestForPayment(redisPaymentDto);
        } else if (redisPaymentDto.status().equals(PaymentStatus.SUCCESS)) {
            paymentProcessingService.approveRequestForPayment(redisPaymentDto);
        }
    }
}

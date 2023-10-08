package faang.school.accountservice.kafka;

import faang.school.accountservice.enums.PaymentStatus;
import faang.school.accountservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.client-id}")
    public void consume(ConsumerRecord<String, Object> record) {
        String operationType = record.key();
        UUID idempotencyKey = (UUID) record.value();
        if (operationType.equals(PaymentStatus.CLEAR.name())) {
            paymentService.clearPayment(idempotencyKey);
        }
        if (operationType.equals(PaymentStatus.REFUND.name())) {
            paymentService.refundPayment(idempotencyKey);
        }
    }
}

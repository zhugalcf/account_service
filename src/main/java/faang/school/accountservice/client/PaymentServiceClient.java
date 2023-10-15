package faang.school.accountservice.client;

import faang.school.accountservice.dto.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${payment-service.host}:${payment-service.port}")
public interface PaymentServiceClient {

    @PostMapping("/payment/handle/answer")
    void sendAnswer(@RequestBody PaymentResponseDto responseDto);
}

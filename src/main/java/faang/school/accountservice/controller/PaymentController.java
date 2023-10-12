package faang.school.accountservice.controller;

import faang.school.accountservice.dto.PaymentDto;
import faang.school.accountservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public void createPayment(@RequestBody PaymentDto paymentDto) {
        paymentService.createPayment(paymentDto);
        log.info("Payment creation with idempotency key {} was initialized", paymentDto.getIdempotencyKey());
    }
}


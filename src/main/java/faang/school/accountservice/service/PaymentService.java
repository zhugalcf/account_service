package faang.school.accountservice.service;

import faang.school.accountservice.dto.RedisPaymentDto;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.model.Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentService {
    private final RequestService requestService;

    public void createRequestForPayment(RedisPaymentDto redisPaymentDto){
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("amount", redisPaymentDto.amount());
        inputData.put("senderBalanceNumber", redisPaymentDto.senderBalanceNumber());
        inputData.put("getterBalanceNumber", redisPaymentDto.getterBalanceNumber());
        inputData.put("currency", redisPaymentDto.currency());
        Request request = Request.builder()
                .userId(redisPaymentDto.userId())
                .requestType(RequestType.CREATE)
                .lockValue(String.valueOf(redisPaymentDto.getterBalanceNumber() + redisPaymentDto.senderBalanceNumber()))
                .isOpenRequest(true)
                .inputData(inputData)
                .requestStatus(RequestStatus.PENDING)
                .version(1).build();
        request = requestService.saveRequestInternal(request);
    }
}

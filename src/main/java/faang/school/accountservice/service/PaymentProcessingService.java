package faang.school.accountservice.service;

import faang.school.accountservice.dto.RedisPaymentDto;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.exception.BalanceException;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.Request;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentProcessingService {
    private final RequestService requestService;
    private final BalanceRepository balanceRepository;

    @Transactional
    public void createRequestForPayment(RedisPaymentDto redisPaymentDto){
        Balance senderBalance = balanceRepository.findById(redisPaymentDto.senderBalanceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Sender balance hasn't found"));
        Balance getterBalance = balanceRepository.findById(redisPaymentDto.senderBalanceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Getter balance hasn't found"));
        if(senderBalance.getCurrentBalance().compareTo(redisPaymentDto.amount()) < 0){
            throw new BalanceException("Not enough money");
        }

        BigDecimal authorizedSenderBalance = senderBalance.getCurrentBalance().subtract(redisPaymentDto.amount());
        senderBalance.setAuthorizationBalance(authorizedSenderBalance);
        balanceRepository.save(senderBalance);

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
        requestService.createRequestInternal(request);
    }

    @Transactional
    public void cancelRequestForPayment(RedisPaymentDto redisPaymentDto){
        Balance senderBalance = balanceRepository.findById(redisPaymentDto.senderBalanceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Sender balance hasn't found"));
        Balance getterBalance = balanceRepository.findById(redisPaymentDto.senderBalanceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Getter balance hasn't found"));
        BigDecimal updatedBalance = senderBalance.getAuthorizationBalance().add(senderBalance.getCurrentBalance());
        senderBalance.setCurrentBalance(updatedBalance);
        balanceRepository.save(senderBalance);

        String lock = String.valueOf(senderBalance.getId() + senderBalance.getId());
        Request request = requestService.getRequestByUserIdAndLock(redisPaymentDto.userId(), lock);
        request.setRequestStatus(RequestStatus.REJECTED);
        request.setIsOpenRequest(false);
        request.setRequestType(RequestType.CLOSE);
        requestService.saveRequestInternal(request);
    }

    @Transactional
    public void approveRequestForPayment(RedisPaymentDto redisPaymentDto){
        Balance senderBalance = balanceRepository.findById(redisPaymentDto.senderBalanceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Sender balance hasn't found"));
        Balance getterBalance = balanceRepository.findById(redisPaymentDto.senderBalanceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Getter balance hasn't found"));

        BigDecimal updatedGetterBalance = getterBalance.getCurrentBalance().add(senderBalance.getAuthorizationBalance());
        senderBalance.setAuthorizationBalance(new BigDecimal(0));
        getterBalance.setCurrentBalance(updatedGetterBalance);
        balanceRepository.save(senderBalance);
        balanceRepository.save(getterBalance);

        String lock = String.valueOf(senderBalance.getId() + senderBalance.getId());
        Request request = requestService.getRequestByUserIdAndLock(redisPaymentDto.userId(), lock);
        request.setRequestStatus(RequestStatus.COMPLETED);
        request.setIsOpenRequest(false);
        request.setRequestType(RequestType.CLOSE);
        requestService.saveRequestInternal(request);
    }
}

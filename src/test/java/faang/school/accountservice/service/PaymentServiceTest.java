package faang.school.accountservice.service;

import faang.school.accountservice.client.PaymentServiceClient;
import faang.school.accountservice.dto.AccountResponseDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.PaymentDto;
import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.exception.CurrencyException;
import faang.school.accountservice.model.request.Request;
import faang.school.accountservice.model.request.RequestStatus;
import faang.school.accountservice.model.request.RequestType;
import faang.school.accountservice.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;
    @Mock
    BalanceService balanceService;
    @Mock
    RequestService requestService;
    @Mock
    AccountService accountService;
    @Mock
    RequestRepository requestRepository;
    @Mock
    PaymentServiceClient paymentServiceClient;
    AccountResponseDto accountResponseDto;
    PaymentDto paymentDto;
    RequestDto requestDto;
    Request request;
    CreateRequestDto createRequestDto;
    UUID uuid;
    BalanceDto balanceDto;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("125e4567-e89b-12d3-a456-426614174000");
        accountResponseDto = AccountResponseDto.builder()
                .id(1L)
                .ownerId(1L)
                .accountNumber("1")
                .ownerType(OwnerType.USER)
                .currency(Currency.RUB)
                .build();
        paymentDto = PaymentDto.builder()
                .amount(new BigDecimal(1000))
                .currency(Currency.RUB)
                .idempotencyKey(uuid)
                .ownerAccountNumber("1")
                .receiverAccountNumber("2")
                .build();
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("receiverAccountNumber", paymentDto.getReceiverAccountNumber());
        inputData.put("ownerAccountNumber", paymentDto.getOwnerAccountNumber());
        inputData.put("currency", paymentDto.getCurrency());
        inputData.put("amount", paymentDto.getAmount());
        requestDto = RequestDto.builder()
                .id(1L)
                .isOpen(true)
                .ownerId(1L)
                .ownerType(OwnerType.USER)
                .idempotencyKey(uuid)
                .requestStatus(RequestStatus.TO_DO)
                .requestType(RequestType.TRANSFER_FUNDS)
                .version(1L)
                .inputData(inputData)
                .build();
        createRequestDto = CreateRequestDto.builder()
                .requestType(RequestType.TRANSFER_FUNDS)
                .idempotencyKey(paymentDto.getIdempotencyKey())
                .ownerType(OwnerType.USER)
                .ownerId(1L)
                .inputData(inputData)
                .build();
        request = Request.builder()
                .id(1L)
                .isOpen(true)
                .ownerId(1L)
                .ownerType(OwnerType.USER)
                .idempotencyKey(uuid)
                .requestStatus(RequestStatus.TO_DO)
                .requestType(RequestType.TRANSFER_FUNDS)
                .version(1L)
                .inputData(inputData)
                .build();
        balanceDto = BalanceDto.builder()
                .id(1L)
                .accountNumber("1")
                .currentAuthorizationBalance(new BigDecimal(10000))
                .currentActualBalance(new BigDecimal(10000))
                .build();
    }

    @Test
    void testBalanceIsAuthorised() {
        when(accountService.getAccountByNumber(any())).thenReturn(accountResponseDto);
        when(requestService.getOrSave(createRequestDto)).thenReturn(requestDto);
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(balanceService.getBalance(1L)).thenReturn(balanceDto);

        paymentService.createPayment(paymentDto);

        verify(balanceService, times(2)).update(any());
        verify(requestService).updateRequestStatus(1L, RequestStatus.IN_PROGRESS, null);
    }

    @Test
    void testCreatePaymentThrownCurrencyException() {
        when(accountService.getAccountByNumber(any())).thenReturn(accountResponseDto);
        paymentDto.setCurrency(Currency.EUR);
        assertThrows(CurrencyException.class, () -> paymentService.createPayment(paymentDto));
    }

    @Test
    void testPaymentWithAnotherRequestStatusNotAuthoriseBalanceTwice() {
        when(accountService.getAccountByNumber(any())).thenReturn(accountResponseDto);
        when(requestService.getOrSave(createRequestDto)).thenReturn(requestDto);
        request.setRequestStatus(RequestStatus.IN_PROGRESS);
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));

        paymentService.createPayment(paymentDto);

        verify(balanceService, never()).update(any());
        verify(requestService, never()).updateRequestStatus(1L, RequestStatus.IN_PROGRESS, null);
    }

    @Test
    void testPaymentIsCleared() {
        Double amount = 1000.0;
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("receiverAccountNumber", paymentDto.getReceiverAccountNumber());
        inputData.put("ownerAccountNumber", paymentDto.getOwnerAccountNumber());
        inputData.put("currency", paymentDto.getCurrency());
        inputData.put("amount", amount);
        request.setRequestStatus(RequestStatus.IN_PROGRESS);
        request.setInputData(inputData);
        when(accountService.getAccountByNumber(any())).thenReturn(accountResponseDto);
        when(requestRepository.findByIdempotencyKey(uuid)).thenReturn(Optional.of(request));
        when(balanceService.getBalance(1L)).thenReturn(balanceDto);

        paymentService.clearPayment(uuid);

        verify(balanceService, times(2)).update(any());
        verify(requestService).updateRequestStatus(1L, RequestStatus.DONE, null);
    }

    @Test
    void testPaymentIsRefund() {
        Double amount = 1000.0;
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("receiverAccountNumber", paymentDto.getReceiverAccountNumber());
        inputData.put("ownerAccountNumber", paymentDto.getOwnerAccountNumber());
        inputData.put("currency", paymentDto.getCurrency());
        inputData.put("amount", amount);
        request.setRequestStatus(RequestStatus.IN_PROGRESS);
        request.setInputData(inputData);
        when(accountService.getAccountByNumber(any())).thenReturn(accountResponseDto);
        when(requestRepository.findByIdempotencyKey(uuid)).thenReturn(Optional.of(request));
        when(balanceService.getBalance(1L)).thenReturn(balanceDto);

        paymentService.refundPayment(uuid);

        verify(balanceService, times(2)).update(any());
        verify(requestService).updateRequestStatus(1L, RequestStatus.DONE, null);
    }
}
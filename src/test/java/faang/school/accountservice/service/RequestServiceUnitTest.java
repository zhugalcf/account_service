package faang.school.accountservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.exception.IdempotencyException;
import faang.school.accountservice.exception.RequestNotFoundException;
import faang.school.accountservice.mapper.request.CreateRequestMapper;
import faang.school.accountservice.mapper.request.CreateRequestMapperImpl;
import faang.school.accountservice.mapper.request.RequestMapper;
import faang.school.accountservice.mapper.request.RequestMapperImpl;
import faang.school.accountservice.model.request.Request;
import faang.school.accountservice.model.request.RequestStatus;
import faang.school.accountservice.model.request.RequestType;
import faang.school.accountservice.repository.RequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RequestServiceUnitTest {

    @InjectMocks
    RequestService requestService;
    @Mock
    RequestRepository requestRepository;
    @Spy
    RequestMapper requestMapper = new RequestMapperImpl();
    @Spy
    CreateRequestMapper createRequestMapper = new CreateRequestMapperImpl();
    @Spy
    ObjectMapper objectMapper = new ObjectMapper();
    CreateRequestDto createRequestDto;
    Request request;
    RequestDto requestDto;

    @BeforeEach
    void setUp() {
        Map<String, Object> exampleInput = new HashMap<>();
        exampleInput.put("1", "1");
        createRequestDto = CreateRequestDto.builder()
                .requestType(RequestType.DEPOSIT_FUNDS)
                .ownerType(OwnerType.USER)
                .idempotencyKey(UUID.fromString("5a8a12c5-3fc0-4f1a-80a5-d9e3a977d953"))
                .inputData(exampleInput)
                .ownerId(1L)
                .build();
        request = Request.builder()
                .id(1L)
                .requestType(RequestType.DEPOSIT_FUNDS)
                .ownerType(OwnerType.USER)
                .idempotencyKey(UUID.fromString("5a8a12c5-3fc0-4f1a-80a5-d9e3a977d953"))
                .inputData(exampleInput)
                .ownerId(1L)
                .requestStatus(RequestStatus.TO_DO)
                .isOpen(true)
                .build();
        requestDto = RequestDto.builder()
                .id(1L)
                .requestType(RequestType.DEPOSIT_FUNDS)
                .ownerType(OwnerType.USER)
                .idempotencyKey(UUID.fromString("5a8a12c5-3fc0-4f1a-80a5-d9e3a977d953"))
                .inputData(exampleInput)
                .ownerId(1L)
                .requestStatus(RequestStatus.TO_DO)
                .isOpen(true)
                .build();
    }


    @Test
    void testGetRequest() {
        Mockito.when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        RequestDto requestResult = requestService.getRequest(1L);
        Assertions.assertEquals(requestDto, requestResult);
    }

    @Test
    void testGetRequestThrowsRequestNotFoundException() {
        Mockito.when(requestRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RequestNotFoundException.class, () -> requestService.getRequest(1L));
    }

    @Test
    void testGetRequestByOwner() {
        Request request2 = Request.builder().id(2L).build();
        List<Request> requests = List.of(request, request2);
        RequestDto requestDto2 = RequestDto.builder().id(2L).build();
        List<RequestDto> requestDtos = List.of(requestDto, requestDto2);

        Mockito.when(requestRepository.findByOwnerIdAndOwnerType(1L, OwnerType.USER)).thenReturn(requests);
        List<RequestDto> requestResult = requestService.getRequestByOwner(1L, OwnerType.USER);
        Assertions.assertEquals(requestDtos, requestResult);
    }

    @Test
    void testIsNewRequestCreating() {
        Mockito.when(requestRepository.findByIdempotencyKey(createRequestDto.getIdempotencyKey()))
                .thenReturn(Optional.empty());
        requestService.getOrSave(createRequestDto);
        Request entity = createRequestMapper.toEntity(createRequestDto);
        entity.setOpen(true);
        entity.setRequestStatus(RequestStatus.TO_DO);
        Mockito.verify(requestRepository).save(entity);
    }

    @Test
    void testIsOldRequestReturned() {
        Mockito.when(requestRepository.findByIdempotencyKey(request.getIdempotencyKey())).thenReturn(Optional.of(request));
        RequestDto requestDtoResult = requestService.getOrSave(createRequestDto);
        Assertions.assertEquals(requestDtoResult, requestDtoResult);
    }

    @Test
    void testCreateRequestThrowsIdempotencyException() {
        createRequestDto.setInputData(new HashMap<>());
        Mockito.when(requestRepository.findByIdempotencyKey(request.getIdempotencyKey())).thenReturn(Optional.of(request));
        Assertions.assertThrows(IdempotencyException.class, () -> requestService.getOrSave(createRequestDto));
    }

    @Test
    void testRequestStatusIsUpdated() {
        RequestStatus requestStatus = RequestStatus.DONE;
        String statusDetails = "some details";
        Mockito.when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        requestDto.setRequestStatus(requestStatus);
        requestDto.setStatusDetails(statusDetails);
        requestDto.setOpen(false);

        RequestDto requestDtoResult = requestService.updateRequestStatus(1L, requestStatus, statusDetails);
        Assertions.assertEquals(requestDto, requestDtoResult);
    }

    @Test
    void testUpdateRequestStatusThrowsRequestNotFoundException() {
        RequestStatus requestStatus = RequestStatus.DONE;
        String statusDetails = "some details";
        Mockito.when(requestRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RequestNotFoundException.class,
                () -> requestService.updateRequestStatus(1L, requestStatus, statusDetails));
    }
}
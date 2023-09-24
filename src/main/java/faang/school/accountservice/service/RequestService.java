package faang.school.accountservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.exception.IdempotencyException;
import faang.school.accountservice.exception.LockedRequestException;
import faang.school.accountservice.exception.RequestNotFoundException;
import faang.school.accountservice.mapper.request.CreateRequestMapper;
import faang.school.accountservice.model.request.Request;
import faang.school.accountservice.mapper.request.RequestMapper;
import faang.school.accountservice.model.request.RequestStatus;
import faang.school.accountservice.repository.RequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;
    private final CreateRequestMapper createRequestMapper;
    private final ObjectMapper objectMapper;

    public RequestDto getRequest(long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(String
                        .format("Request with id: %d not found", requestId)));
        return requestMapper.toDto(request);
    }

    public List<RequestDto> getRequestByOwner(long ownerId, OwnerType ownerType) {
        List<Request> request = requestRepository.findByOwnerIdAndOwnerType(ownerId, ownerType);
        return requestMapper.toListDto(request);
    }

    @Transactional
    public RequestDto getOrSave(CreateRequestDto requestDto) {
        Request incomeRequest = createRequestMapper.toEntity(requestDto);
        Request request = requestRepository.findByIdempotencyKey(requestDto.getIdempotencyKey()).orElse(null);
        if (request != null) {
            validateSameTokenDifferentInput(requestDto.getInputData(), request.getInputData());
            return requestMapper.toDto(request);
        }
        incomeRequest.setRequestStatus(RequestStatus.TO_DO);
        incomeRequest.setOpen(true);
        return requestMapper.toDto(requestRepository.save(incomeRequest));
    }

    @Transactional
    public RequestDto updateRequestStatus(long requestId, RequestStatus requestStatus, String statusDetails) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(String
                        .format("Request with id: %d not found", requestId)));
        if (requestStatus != null) {
            request.setRequestStatus(requestStatus);
            request.setStatusDetails(statusDetails);
            if (requestStatus.isTerminated()) {
                request.setOpen(false);
            }
        }
        return requestMapper.toDto(request);
    }

    private void validateSameTokenDifferentInput(Object newInput, Map<String, Object> oldInput) {
        boolean inputSame = isInputDataSame(newInput, oldInput);
        if (!inputSame) {
            throw new IdempotencyException("You already have the request with same idempotent token");
        }
    }

    private boolean isInputDataSame(Object newInput, Map<String, Object> oldInput) {
        try {
            String newInputData = objectMapper.writeValueAsString(newInput);
            String oldInputData = objectMapper.writeValueAsString(oldInput);
            return newInputData.equals(oldInputData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

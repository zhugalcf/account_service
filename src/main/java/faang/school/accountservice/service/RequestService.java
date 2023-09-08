package faang.school.accountservice.service;

import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.ResponseRequestDto;
import faang.school.accountservice.dto.request.OpenRequestDto;
import faang.school.accountservice.dto.request.UpdateRequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.publisher.ExecuteRequestPublisher;
import faang.school.accountservice.publisher.RequestStatusPublisher;
import faang.school.accountservice.repository.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    @Value("${spring.data.redis.channels.execute-request-channel.name}")
    private String executeRequestChannel;
    @Value("${spring.data.redis.channels.request_status_channel.name}")
    private String requestStatusChannel;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final RequestStatusPublisher requestStatusPublisher;
    private final ExecuteRequestPublisher executeRequestPublisher;

    @Transactional
    public List<ResponseRequestDto> getByUserId(Long userId) {
        return requestMapper.toListDto(requestRepository.findAllByUserId(userId));
    }

    @Transactional
    public ResponseRequestDto createRequest(CreateRequestDto createRequestDto) {
        Request request = requestMapper.fromCreateToEntity(createRequestDto);
        Request requestFromDb = requestRepository.findByIdempotentToken(createRequestDto.getIdempotentToken()).orElse(null);

        if (requestFromDb != null) {
            validateSameTokenDifferentInput(request.getInput(), requestFromDb.getInput());
            return requestMapper.toDto(requestFromDb);
        }

        request.setStatus(RequestStatus.WAITING);

        requestStatusPublisher.publishMessage(requestMapper.toStatusChangeEvent(request), requestStatusChannel);
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    public ResponseRequestDto openRequest(OpenRequestDto openRequestDto) {
        Request request = requestRepository.findByIdempotentTokenForUpdate(openRequestDto.getIdempotentToken())
                .orElseThrow(() -> new EntityNotFoundException("Request is not found"));
        Request withSameLock = requestRepository.findByLockAndUserIdAndIsOpenTrue(openRequestDto.getLock(), openRequestDto.getUserId())
                .orElse(null);

        if (!request.getStatus().equals(RequestStatus.WAITING)) {
            return requestMapper.toDto(request);
        }

        validateRequestWithSameLock(request, withSameLock);

        request.setOpen(true);
        request.setStatus(RequestStatus.TOEXECUTE);

        requestStatusPublisher.publishMessage(requestMapper.toStatusChangeEvent(request), requestStatusChannel);
        return requestMapper.toDto(request);
    }

    @Transactional
    public ResponseRequestDto updateRequest(UpdateRequestDto updateRequestDto) {
        Request request = requestRepository.findByIdempotentTokenForUpdate(updateRequestDto.getIdempotentToken())
                .orElseThrow(() -> new EntityNotFoundException("Request is not found"));
        RequestStatus requestStatus = request.getStatus();

        if (requestStatus.equals(RequestStatus.EXECUTED) || requestStatus.equals(RequestStatus.CANCELLED)) {
            return requestMapper.toDto(request);
        }

        if (requestStatus.equals(RequestStatus.WAITING)
                && updateRequestDto.getCancel() != null
                && updateRequestDto.getCancel()) {
            request.setStatus(RequestStatus.CANCELLED);
            request.setOpen(false);
        }

        if (requestStatus.equals(RequestStatus.TOEXECUTE)) {
            request.setStatus(RequestStatus.WAITING);
        }

        request.setDetails(updateRequestDto.getDetails());
        request.setInput(updateRequestDto.getInput());

        requestStatusPublisher.publishMessage(requestMapper.toStatusChangeEvent(request), requestStatusChannel);
        return requestMapper.toDto(request);
    }

    @Transactional
    public void executeRequests() {
        List<Request> requests = requestRepository.findAllByStatusForUpdate(RequestStatus.TOEXECUTE);
        requests.forEach(request -> {
            executeRequestPublisher.publishMessage(requestMapper.toExecuteEvent(request), executeRequestChannel);
            request.setStatus(RequestStatus.EXECUTED);
            request.setOpen(false);
        });
    }

    private void validateSameTokenDifferentInput(Map<String, Object> newInput, Map<String, Object> oldInput) {
        boolean inputNotSame = oldInput.entrySet()
                .stream()
                .noneMatch(entry -> newInput.containsKey(entry.getKey())
                        && newInput.containsValue(entry.getValue()));

        if (inputNotSame) {
            throw new IllegalArgumentException("You already have the request with same idempotent token");
        }
    }


    private static void validateRequestWithSameLock(Request request, Request withSameLock) {
        if (withSameLock != null && !withSameLock.getIdempotentToken().equals(request.getIdempotentToken())) {
            throw new IllegalArgumentException("You are already processing the request with same lock");
        }
    }
}

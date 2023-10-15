package faang.school.accountservice.service;

import faang.school.accountservice.config.context.UserContext;
import faang.school.accountservice.dto.request.CloseRequestDto;
import faang.school.accountservice.dto.request.OpenRequestDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.dto.request.UpdateRequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.messaging.publisher.RequestEventPublisher;
import faang.school.accountservice.repository.RequestRepository;
import faang.school.accountservice.validation.RequestValidation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RequestService {
    private static final String REQUEST_NOT_EXISTS = "Request by id: %s does not exist";

    private final RequestEventPublisher requestEventPublisher;
    private final RequestRepository requestRepository;
    private final RequestValidation requestValidation;
    private final RequestMapper requestMapper;
    private final UserContext userContext;

    @Transactional
    public RequestDto createRequest(RequestDto requestDto) {
        Request request = requestMapper.toEntity(requestDto);
        request.setUserId(userContext.getUserId());
        request.setStatus(RequestStatus.WAITING);

        request = requestRepository.save(request);
        log.info("Request: {} (user: {}) placed in waiting status", request.getRequestId(), request.getUserId());
        return requestMapper.toDto(request);
    }

    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 10), retryFor = PersistenceException.class)
    public RequestDto updateRequest(UpdateRequestDto updateRequestDto) {
        Request request = requestRepository.findById(updateRequestDto.getRequestId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(REQUEST_NOT_EXISTS, updateRequestDto.getRequestId())));
        
        if (requestValidation.checkRelevance(request)) {
            log.info("Request: {} (user: {}) has already been executed or canceled",
                    request.getRequestId(), request.getUserId());
            return requestMapper.toDto(requestRepository.save(request));
        }
        request.setAdditionally(updateRequestDto.getAdditionally());
        request.setInput(updateRequestDto.getInput());

        request = requestRepository.save(request);
        log.info("Request: {} (user: {}) has been updated", request.getRequestId(), request.getUserId());
        return requestMapper.toDto(request);
    }

    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5), retryFor = PersistenceException.class)
    public RequestDto openRequest(OpenRequestDto openRequestDto) {
        Request request = requestRepository.findById(openRequestDto.getRequestId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(REQUEST_NOT_EXISTS, openRequestDto.getRequestId())));

        if (requestValidation.validateOpeningRequest(openRequestDto, request)) {
            log.info("Request: {} (user: {}) has already been opened", request.getRequestId(), request.getUserId());
            return requestMapper.toDto(request);
        }
        request.setOpen(true);
        request.setStatus(RequestStatus.TO_EXECUTE);
        request.setLock(openRequestDto.getLock());

        request = requestRepository.save(request);
        log.info("Request: {} (user: {}) is open", request.getRequestId(), request.getUserId());
        return requestMapper.toDto(request);
    }

    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 3), retryFor = PersistenceException.class)
    public RequestDto closeRequest(CloseRequestDto closeRequestDto) {
        Request request = requestRepository.findById(closeRequestDto.getRequestId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(REQUEST_NOT_EXISTS, closeRequestDto.getRequestId())));
        if (requestValidation.checkCurrentStatus(request)) {
            log.info("Request: {} already has a status: {}", request.getRequestId(), request.getStatus());
            return requestMapper.toDto(request);
        }

        int ordinaryStatus = closeRequestDto.getStatus();
        if (requestValidation.checkTypeOfStatusToChange(ordinaryStatus, request)) {
            log.warn("Incorrect status transmitted: {} ({}). Current status: {}",
                    ordinaryStatus, RequestStatus.of(ordinaryStatus), request.getStatus());
            throw new IllegalArgumentException("Incorrect type of request transmitted: " + ordinaryStatus);
        }
        request.setStatus(RequestStatus.of(ordinaryStatus));
        request.setOpen(false);
        request.setLock(null);

        request = requestRepository.save(request);
        log.info("Request: {} has been closed", request.getRequestId());
        return requestMapper.toDto(request);
    }

    @Transactional
    public void executeRequests() {
        List<Request> requests = requestRepository.findAllWithLimit();
        log.info("Thread: {} has started executing a request packet", Thread.currentThread().getName());
        requests.forEach(request -> {
            request.setStatus(RequestStatus.IN_PROGRESS);
            requestRepository.save(request);
            requestEventPublisher.publish(request);
        });
        log.info("Thread: {} has completed the request packets", Thread.currentThread().getName());
    }
}

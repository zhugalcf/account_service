package faang.school.accountservice.service;

import faang.school.accountservice.config.context.UserContext;
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

        log.info("Request: {} (user: {}) placed in waiting status", request.getRequestId(), request.getUserId());
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 10), retryFor = PersistenceException.class)
    public RequestDto updateRequest(UpdateRequestDto updateRequestDto) {
        Request request = requestRepository.findById(updateRequestDto.getRequestId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Request by id: %s does not exist", updateRequestDto.getRequestId())));
        request.setAdditionally(updateRequestDto.getAdditionally());
        
        if (requestValidation.checkRelevance(request)) {
            log.info("Request: {} (user: {}) has already been executed or canceled",
                    request.getRequestId(), request.getUserId());
            return requestMapper.toDto(requestRepository.save(request));
        }
        requestValidation.validateClosureRequest(updateRequestDto, request);

        request.setInput(updateRequestDto.getInput());
        log.info("Request: {} (user: {}) has been updated", request.getRequestId(), request.getUserId());
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5), retryFor = PersistenceException.class)
    public RequestDto openRequest(OpenRequestDto openRequestDto) {
        Request request = requestRepository.findById(openRequestDto.getRequestId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Request by id: %s does not exist", openRequestDto.getRequestId())));

        if (requestValidation.validateOpeningRequest(openRequestDto, request)) {
            log.info("Request: {} (user: {}) has already been opened", request.getRequestId(), request.getUserId());
            return requestMapper.toDto(request);
        }
        request.setOpen(true);
        request.setStatus(RequestStatus.TO_EXECUTE);
        request.setLock(openRequestDto.getLock());
        log.info("Request: {} (user: {}) is open", request.getRequestId(), request.getUserId());
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    public void executeRequests() {
        List<Request> requests = requestRepository.findAllWithLimit();
        log.info("Thread: {} has started executing a request packet", Thread.currentThread().getName());
        requests.forEach(request -> {
            requestEventPublisher.publish(request);
            request.setStatus(RequestStatus.IN_EXECUTION);
        });
        log.info("Thread: {} has completed the request packets", Thread.currentThread().getName());
        requestRepository.saveAll(requests);
    }
}

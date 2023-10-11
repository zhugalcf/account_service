package faang.school.accountservice.service;

import faang.school.accountservice.config.context.UserContext;
import faang.school.accountservice.dto.request.OpenRequestDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.dto.request.UpdateRequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.exception.RequestLockIsOccupiedException;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.repository.RequestRepository;
import faang.school.accountservice.validate.RequestValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestValidation requestValidation;
    private final RequestMapper requestMapper;
    private final UserContext userContext;

    @Transactional
    public RequestDto createRequest(RequestDto requestDto) {
        Request request = requestMapper.toEntity(requestDto);
        request.setUserId(userContext.getUserId());
        request.setStatus(RequestStatus.WAITING);

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
            return requestMapper.toDto(requestRepository.save(request));
        }
        requestValidation.validateClosureRequest(updateRequestDto, request);

        request.setInput(updateRequestDto.getInput());
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5), retryFor = PersistenceException.class)
    public RequestDto openRequest(OpenRequestDto openRequestDto) {
        Request request = requestRepository.findById(openRequestDto.getRequestId())
                .orElseThrow(EntityNotFoundException::new);
        if (requestValidation.validateOpeningRequest(openRequestDto, request)) {
            return requestMapper.toDto(request);
        }

        Request withSameLock = requestRepository
                .findByUserIdAndLockAndOpenIsTrue(openRequestDto.getLock(), request.getUserId())
                .orElse(null);
        if (withSameLock != null) {
            throw new RequestLockIsOccupiedException(String.format(
                    "The lock is busy with the request: %s", withSameLock.getRequestId()));
        }

        request.setOpen(true);
        request.setStatus(RequestStatus.TO_EXECUTE);
        request.setLock(openRequestDto.getLock());
        return requestMapper.toDto(requestRepository.save(request));
    }
}

package faang.school.accountservice.service;

import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.dto.UpdateRequestDto;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.errors.MessagesForUsers;
import faang.school.accountservice.exception.TooManyAttemptsException;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.message.publisher.CreateRequestPublisher;
import faang.school.accountservice.message.publisher.ExecuteRequestPublisher;
import faang.school.accountservice.message.publisher.OpenRequestPublisher;
import faang.school.accountservice.message.publisher.UpdateRequestPublisher;
import faang.school.accountservice.model.Request;
import faang.school.accountservice.repository.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final RequestMapper requestMapper;
    private final CreateRequestPublisher createRequestPublisher;
    private final OpenRequestPublisher openRequestPublisher;
    private final UpdateRequestPublisher updateRequestPublisher;
    private final ExecuteRequestPublisher executeRequestPublisher;
    @Value("${request.schedule.period}")
    private Long requiredPeriod;
    @Value("${request.max-number-of-requests}")
    private Long maxNumberOfRequests;
    @Value("${request.number-of-requests-for-executing}")
    private long limit;
    static List<Long> usersHavingTooMuchRequests;

    @Transactional
    public RequestDto createRequest(RequestDto requestDto) {
        if(usersHavingTooMuchRequests.contains(requestDto.getUserId())){
            throw new TooManyAttemptsException(MessagesForUsers.TOO_MANY_ATTEMPTS);
        }
        Request request = requestMapper.toEntity(requestDto);
        try {
            request = createRequestInDB(request);
            requestDto = saveRequestIntoDB(request, requestDto);
            createRequestPublisher.publish(requestDto);
            return requestDto;
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            requestDto.setRequestStatus(RequestStatus.REJECTED);
            requestDto.setAdditionalData(MessagesForUsers.TOO_MANY_ATTEMPTS);
            createRequestPublisher.publish(requestDto);
            return requestDto;
        }
    }

    @Transactional
    public UpdateRequestDto openRequest(UpdateRequestDto updateRequestDto) {
        Request request = findRequestInDB(updateRequestDto);

        if (!request.getRequestStatus().equals(RequestStatus.PENDING)) {
            return requestMapper.toUpdateDto(request);
        }

        request.setIsOpenRequest(true);
        request.setRequestStatus(RequestStatus.OPEN);
        try {
            updateRequestDto = updateRequestIntoDB(request, updateRequestDto);
            openRequestPublisher.publish(updateRequestDto);
            return updateRequestDto;
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            updateRequestDto.setRequestStatus(RequestStatus.REJECTED);
            updateRequestDto.setAdditionalData(MessagesForUsers.TOO_MANY_ATTEMPTS);
            openRequestPublisher.publish(updateRequestDto);
            return updateRequestDto;
        }
    }

    @Transactional
    public UpdateRequestDto updateRequest(UpdateRequestDto updateRequestDto) {
        Request request = findRequestInDB(updateRequestDto);
        request = requestMapper.updateEntityFromUpdateDto(updateRequestDto, request);
        updateRequestDto = updateRequestIntoDB(request, updateRequestDto);
        updateRequestPublisher.publish(updateRequestDto);
        return updateRequestDto;
    }

    @Transactional(readOnly = true)
    public List<RequestDto> getByUserId(Long userId) {
        return requestMapper.entityListToDtoList(repository.findAllByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException("User doesn't have any requests")));
    }

    @Scheduled(fixedRateString = "${request.schedule.period}")
    @Transactional
    @Async(value = "requestsPool")
    public void executeRequests() {
        List<Request> pendingRequests = repository.findSomeRequestsForExecute(limit);
        pendingRequests = pendingRequests.stream().peek(request -> {
                    request.setRequestStatus(RequestStatus.COMPLETED);
                    request.setIsOpenRequest(false);
                })
                .collect(Collectors.toList());
        try {
            saveSeveralRequests(pendingRequests);
        } catch (OptimisticLockException e){
            log.error(e.getMessage());
        }
        pendingRequests.forEach(request -> executeRequestPublisher.publish(requestMapper.entityToExecuteDto(request)));
    }

    @Scheduled(fixedRateString = "${request.schedule.period}")
    @Transactional
    public void checkRequestNumber() {
        ZonedDateTime requiredTime = ZonedDateTime.now().minusNanos(requiredPeriod);
        usersHavingTooMuchRequests = repository.findAllGroupedByUserIdForPeriod(requiredTime, maxNumberOfRequests)
                .orElse(null);
    }

    private Request findRequestInDB(UpdateRequestDto updateRequestDto) {
        return repository.findByIdForUpdate(updateRequestDto.getId()).orElseThrow((() ->
                new EntityNotFoundException(String.format("Request with ID: %d haven't found", updateRequestDto.getId()))));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UpdateRequestDto updateRequestIntoDB(Request request, UpdateRequestDto requestDto) {
        try {
            return requestMapper.toUpdateDto(repository.save(request));
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            requestDto.setRequestStatus(RequestStatus.REJECTED);
            requestDto.setAdditionalData(MessagesForUsers.TOO_MANY_ATTEMPTS);
            return requestDto;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RequestDto saveRequestIntoDB(Request request, RequestDto requestDto) {
        try {
            return requestMapper.toDto(repository.save(request));
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            requestDto.setRequestStatus(RequestStatus.REJECTED);
            requestDto.setAdditionalData(MessagesForUsers.TOO_MANY_ATTEMPTS);
            return requestDto;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSeveralRequests(List<Request> requests) {
        try {
            repository.saveAll(requests);
        } catch (OptimisticLockException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Request was updated by other thread, so it can't be saved");
        }
    }

    @Transactional
    public Request createRequestInternal(Request request) {
        if(usersHavingTooMuchRequests.contains(request.getUserId())){
            throw new TooManyAttemptsException(MessagesForUsers.TOO_MANY_ATTEMPTS);
        }
        try {
            return createRequestInDB(request);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            request.setRequestStatus(RequestStatus.REJECTED);
            request.setAdditionalData(MessagesForUsers.TOO_MANY_ATTEMPTS);
            return request;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Request createRequestInDB(Request request){
        request.setRequestType(RequestType.CREATE);
        request.setRequestStatus(RequestStatus.PENDING);
        request.setIsOpenRequest(false);
        request.setVersion(1);
        request.setLockValue(String.format("%d%s", request.getUserId(), RequestType.CREATE));
        return repository.save(request);
    }

    @Transactional
    public Request saveRequestInternal(Request request){
        return repository.save(request);
    }

    @Transactional
    public Request getRequestByUserIdAndLock(Long userId, String lock){
        return repository.findByUserIdAndLockValue(userId, lock);
    }
}

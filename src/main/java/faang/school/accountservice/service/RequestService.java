package faang.school.accountservice.service;

import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.dto.UpdateRequestDto;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.errors.MessagesForUsers;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.message.publisher.CreateRequestPublisher;
import faang.school.accountservice.message.publisher.ExecuteRequestPublisher;
import faang.school.accountservice.message.publisher.OpenRequestPublisher;
import faang.school.accountservice.message.publisher.RequestNumberPublisher;
import faang.school.accountservice.message.publisher.UpdateRequestPublisher;
import faang.school.accountservice.model.Request;
import faang.school.accountservice.repository.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final RequestMapper requestMapper;
    private final RequestNumberPublisher requestNumberPublisher;
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

    @Transactional
    public RequestDto createRequest(RequestDto requestDto) {
        Request request = requestMapper.toEntity(requestDto);
        request.setRequestType(RequestType.CREATE);
        request.setRequestStatus(RequestStatus.PENDING);
        request.setIsOpenRequest(false);
        request.setVersion(1);
        request.setLockValue(String.format("%d%s", requestDto.getUserId(), RequestType.CREATE));
        try {
            request = repository.save(request);
            createRequestPublisher.publish(requestDto);
            return requestMapper.toDto(request);
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
            request = repository.save(request);
            openRequestPublisher.publish(updateRequestDto);
            return requestMapper.toUpdateDto(request);
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
        updateRequestDto = saveRequestIntoDB(request, updateRequestDto);
        updateRequestPublisher.publish(updateRequestDto);
        return updateRequestDto;
    }

    @Transactional(readOnly = true)
    public List<RequestDto> getByUserId(Long userId){
        return requestMapper.entityListToDtoList(repository.findAllByUserId(userId));
    }

    @Scheduled(fixedRateString = "${request.schedule.period}")
    @Transactional
    public void executeRequests() {
        List<Request> pendingRequests = repository.findSomeRequestsForExecute(limit);
        pendingRequests.forEach(request -> {
            executeRequestPublisher.publish(requestMapper.entityToExecuteDto(request));
            request.setRequestStatus(RequestStatus.COMPLETED);
            request.setIsOpenRequest(false);
            repository.save(request);
        });
    }

    @Scheduled(fixedRateString = "${request.schedule.period}")
    private void checkRequestNumber() {
        ZonedDateTime requiredTime = ZonedDateTime.now().minusNanos(requiredPeriod);
        Map<Long, Long> usersHavingTooMuchRequests =
                repository.findAllGroupedByUserIdForPeriod(requiredTime, maxNumberOfRequests);
        usersHavingTooMuchRequests.keySet()
                .forEach(user -> requestNumberPublisher.publish(RequestDto.builder().userId(user)
                        .requestStatus(RequestStatus.REJECTED)
                        .additionalData(MessagesForUsers.TOO_MANY_ATTEMPTS).build()));
    }

    private Request findRequestInDB(UpdateRequestDto updateRequestDto) {
        return repository.findByIdForUpdate(updateRequestDto.getId()).orElseThrow((() ->
                new EntityNotFoundException(String.format("Request with ID: %d haven't found", updateRequestDto.getId()))));
    }

    private UpdateRequestDto saveRequestIntoDB(Request request, UpdateRequestDto requestDto) {
        try {
            return requestMapper.toUpdateDto(repository.save(request));
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            requestDto.setRequestStatus(RequestStatus.REJECTED);
            requestDto.setAdditionalData(MessagesForUsers.TOO_MANY_ATTEMPTS);
            return requestDto;
        }
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

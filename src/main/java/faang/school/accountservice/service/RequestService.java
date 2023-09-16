package faang.school.accountservice.service;

import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.dto.UpdateRequestDto;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.errors.MessagesForUsers;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.message.RequestNumberPublisher;
import faang.school.accountservice.model.Request;
import faang.school.accountservice.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final RequestMapper requestMapper;
    private final RequestNumberPublisher requestNumberPublisher;
    @Value("${request.schedule.period}")
    private Long requiredPeriod;
    @Value("${request.max-number-of-requests}")
    private Long maxNumberOfRequests;

    @Transactional
    public RequestDto createRequest(RequestDto requestDto){
        Request request = requestMapper.toEntity(requestDto);
        request.setRequestType(RequestType.CREATE);
        request.setIsOpenRequest(true);
        request.setVersion(1);
        try {
            return requestMapper.toDto(repository.save(request));
        } catch (DataIntegrityViolationException e){
            log.error(e.getMessage());
            requestDto.setRequestStatus(RequestStatus.REJECTED);
            requestDto.setAdditionalData(MessagesForUsers.TOO_MANY_ATTEMPTS);
            return requestDto;
        }
    }

    @Transactional
    public UpdateRequestDto updateStatus(UpdateRequestDto requestDto){
        Request request = findRequestInDB(requestDto);
        request.setRequestStatus(requestDto.getRequestStatus());
        return saveRequestIntoDB(request, requestDto);
    }

    @Transactional
    public UpdateRequestDto updateFlag(UpdateRequestDto requestDto){
        Request request = findRequestInDB(requestDto);
        request.setIsOpenRequest(!request.getIsOpenRequest());
        return saveRequestIntoDB(request, requestDto);
    }

    @Transactional
    public UpdateRequestDto updateInputData(UpdateRequestDto requestDto){
        Request request = findRequestInDB(requestDto);
        request.setInputData(requestDto.getInputData());
        return saveRequestIntoDB(request, requestDto);
    }

    private Request findRequestInDB(UpdateRequestDto requestDto){
        return repository.findByUserIdAndLockValue(requestDto.getUserId(), requestDto.getLockValue());
    }
    private UpdateRequestDto saveRequestIntoDB(Request request, UpdateRequestDto requestDto){
        try {
            return requestMapper.toUpdateDto(repository.save(request));
        } catch (DataIntegrityViolationException e){
            log.error(e.getMessage());
            requestDto.setRequestStatus(RequestStatus.REJECTED);
            requestDto.setAdditionalData(MessagesForUsers.TOO_MANY_ATTEMPTS);
            return requestDto;
        }
    }

    @Scheduled(fixedRateString = "${request.schedule.period}")
    private void checkRequestNumber(){
        ZonedDateTime requiredTime = ZonedDateTime.now().minusNanos(requiredPeriod);
        Map<Long, Long> usersHavingTooMuchRequests =
                repository.findAllGroupedByUserIdForPeriod(requiredTime, maxNumberOfRequests);
        usersHavingTooMuchRequests.keySet()
                .forEach(user -> requestNumberPublisher.publish(RequestDto.builder().userId(user)
                        .requestStatus(RequestStatus.REJECTED)
                        .additionalData(MessagesForUsers.TOO_MANY_ATTEMPTS).build()));
    }

}

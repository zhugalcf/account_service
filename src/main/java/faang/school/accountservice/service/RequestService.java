package faang.school.accountservice.service;

import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.repository.RequestRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository repository;
    private final RequestMapper requestMapper;

    public RequestDto getRequest(long id) {
        Optional<Request> requestById = repository.findById(id);
        validateRequest(requestById);
        return requestMapper.toDto(requestById.get());
    }

    @Transactional
    public RequestDto getOrSaveRequest(Request request) {
        RequestDto requestDto = requestMapper.toDto(request);
        Request build = build(requestDto);

        Optional<Request> requestById = repository.findById(build.getId());
        if (requestById.isEmpty()) {
            repository.save(build);
            return requestMapper.toDto(build);
        }
        return requestMapper.toDto(requestById.get());
    }

    @Transactional
    public RequestDto changeStatus(long id, RequestStatus requestStatus, String statusDetails) {
        Optional<Request> requestById = repository.findById(id);
        Request request = requestById.get();
        if (requestStatus == RequestStatus.FAILED) {
            request.setActive(false);
        }
        validateRequest(requestById);
        request.setRequestStatus(requestStatus);
        request.setDetails(statusDetails);
        repository.save(request);
        return requestMapper.toDto(request);
    }

    private Request build(RequestDto requestDto) {
        return Request.builder()
                .id(requestDto.getRequestId())
                .userId(requestDto.getUserId())
                .lockValue(requestDto.getLockValue())
                .active(requestDto.isActive())
                .details(requestDto.getDetails())
                .version(requestDto.getVersion())
                .requestType(requestDto.getRequestType())
                .requestStatus(requestDto.getRequestStatus())
                .build();
    }

    private void validateRequest(Optional<Request> requestById) {
        if (requestById.isEmpty()) {
            log.error("request doesn't exist");
            throw new IllegalArgumentException("request doesn't exist");
        }
    }
}

package faang.school.accountservice.service.request;

import faang.school.accountservice.config.context.UserContext;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.entity.request.RequestStatus;
import faang.school.accountservice.entity.request.RequestType;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserContext userContext;

    @Transactional
    public RequestDto createRequest(RequestDto requestDto) {
        return requestMapper.toDto(requestRepository.save(requestMapper.toEntity(requestDto)));
    }

    @Transactional(readOnly = true)
    public List<RequestDto> findByUserId(Long userId) {
        return requestRepository.findByUserId(userId).stream().map(requestMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<RequestDto> findMyRequests() {
        Long userId = userContext.getUserId();
        return requestRepository.findByUserId(userId).stream().map(requestMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<RequestDto> findByRequestStatus(RequestStatus requestStatus) {
        return requestRepository.findByRequestStatus(requestStatus).stream().map(requestMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<RequestDto> findByRequestType(RequestType requestType) {
        return requestRepository.findByRequestType(requestType).stream().map(requestMapper::toDto).toList();
    }

    @Transactional
    public void updateRequest(RequestDto requestDto) {
        requestRepository.save(requestMapper.toEntity(requestDto));
    }

    @Transactional
    public void deleteRequest(RequestDto requestDto) {
        requestRepository.delete(requestMapper.toEntity(requestDto));
    }
}

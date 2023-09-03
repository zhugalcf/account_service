package faang.school.accountservice.service;

import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.exception.RequestNotFoundException;
import faang.school.accountservice.model.request.Request;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;

    public RequestDto getRequest(long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(String.format("Request with id: %d not found", requestId)));
        return requestMapper.toDto(request);
    }
}

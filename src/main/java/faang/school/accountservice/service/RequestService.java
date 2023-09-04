package faang.school.accountservice.service;

import faang.school.accountservice.dto.ResponseRequestDto;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Transactional
    public List<ResponseRequestDto> getByUserId(Long userId) {
        return requestMapper.toListDto(requestRepository.findAllByUserId(userId));
    }
}

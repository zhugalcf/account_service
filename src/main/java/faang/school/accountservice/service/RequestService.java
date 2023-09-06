package faang.school.accountservice.service;

import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.ResponseRequestDto;
import faang.school.accountservice.dto.request.OpenRequestDto;
import faang.school.accountservice.dto.request.UpdateRequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.repository.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Transactional
    public ResponseRequestDto createRequest(CreateRequestDto createRequestDto) {
        Request request = requestMapper.fromCreateToEntity(createRequestDto);
        Request requestFromDb = requestRepository.findByIdempotentToken(createRequestDto.getIdempotentToken()).orElse(null);

        if (requestFromDb != null) {
            validateSameTokenDifferentInput(request.getInput(), requestFromDb.getInput());
            return requestMapper.toDto(requestFromDb);
        }

        request.setStatus(RequestStatus.TODO);
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    public ResponseRequestDto openRequest(OpenRequestDto openRequestDto) {
        Request request = requestRepository.findByIdempotentToken(openRequestDto.getIdempotentToken())
                .orElseThrow(() -> new EntityNotFoundException("Request is not found"));

        if (!request.getStatus().equals(RequestStatus.TODO)){
            return requestMapper.toDto(request);
        }

        Request withSameLock = requestRepository.findByLockAndUserIdAndIsOpenTrue(openRequestDto.getLock(), openRequestDto.getUserId())
                .orElse(null);

        if (withSameLock != null && !withSameLock.getIdempotentToken().equals(request.getIdempotentToken())) {
            throw new IllegalArgumentException("You are already processing the request with same lock");
        }

        request.setOpen(true);
        request.setStatus(RequestStatus.WAITING);
        request.setVersion(request.getVersion() + 1);

        return requestMapper.toDto(request);
    }

    @Transactional
    public ResponseRequestDto updateRequest(UpdateRequestDto updateRequestDto) {
        Request request = requestRepository.findByIdempotentToken(updateRequestDto.getIdempotentToken())
                .orElseThrow(() -> new EntityNotFoundException("Request is not found"));
        if(request.getStatus().equals(RequestStatus.TODO) || request.getStatus().equals(RequestStatus.WAITING)){
            request.setDetails(updateRequestDto.getDetails());
            request.setInput(updateRequestDto.getInput());
            if (updateRequestDto.getCancel() != null && updateRequestDto.getCancel()){
                request.setStatus(RequestStatus.CANCELLED);
            }
        }

        return requestMapper.toDto(request);
    }

    private void validateSameTokenDifferentInput(Map<String, Object> newInput, Map<String, Object> oldInput) {
        boolean inputNotSame = oldInput.entrySet()
                .stream()
                .noneMatch(entry -> newInput.containsKey(entry.getKey())
                        && newInput.containsValue(entry.getValue()));

        if (inputNotSame) {
            throw new IllegalArgumentException("You already have the request with same idempotent token");
        }
    }
}

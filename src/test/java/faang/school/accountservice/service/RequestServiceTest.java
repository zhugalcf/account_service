package faang.school.accountservice.service;

import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.enums.RequestType;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @InjectMocks
    private RequestService requestService;

    @Mock
    private RequestRepository repository;

    @Spy
    private RequestMapper requestMapper;

    private RequestDto requestDto;
    private Request request;

    @BeforeEach
    void setUp() {
        requestDto = new RequestDto();
        requestDto.setRequestId(3L);
        requestDto.setUserId(2L);
        requestDto.setRequestStatus(RequestStatus.COMPLETE);
        requestDto.setVersion(1L);
        requestDto.setActive(true);
        requestDto.setLockValue(2L);
        requestDto.setRequestType(RequestType.POST);

        request = new Request();
        request.setId(requestDto.getRequestId());
        request.setUserId(requestDto.getUserId());
        request.setRequestType(requestDto.getRequestType());
        request.setRequestStatus(requestDto.getRequestStatus());
        request.setVersion(requestDto.getVersion());
        request.setActive(request.isActive());
        request.setLockValue(requestDto.getLockValue());

    }

    @Test
    void getRequest() {
        Mockito.when(repository.findById(requestDto.getRequestId()))
                .thenReturn(Optional.of(request));
        Mockito.when(requestMapper.toDto(request))
                .thenReturn(requestDto);

        RequestDto req = requestService.getRequest(requestDto);
        assertNotNull(req);
        assertEquals(requestDto, req);

    }

    @Test
    void getRequest_IdNotExist() {
        Mockito.when(repository.findById(requestDto.getRequestId()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> requestService.getRequest(requestDto));
    }

    @Test
    void getOrSaveRequest() {
        Mockito.when(repository.findById(3L))
                .thenReturn(Optional.empty());
        Mockito.when(requestMapper.toEntity(requestDto))
                .thenReturn(request);

        requestService.getOrSaveRequest(requestDto, 3L);
        Mockito.verify(repository, Mockito.times(1)).save(request);
    }

    @Test
    void changeStatus() {
        Mockito.when(repository.findById(requestDto.getRequestId()))
                .thenReturn(Optional.of(request));
        Mockito.when(requestMapper.toDto(request))
                .thenReturn(requestDto);

        RequestDto expected = requestService.changeStatus(requestDto);
        assertEquals(RequestStatus.COMPLETE, expected.getRequestStatus());

        Mockito.verify(repository, Mockito.times(1)).save(request);
    }
}
package faang.school.accountservice.service.request;

import faang.school.accountservice.config.context.UserContext;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.entity.request.Request;
import faang.school.accountservice.entity.request.RequestStatus;
import faang.school.accountservice.entity.request.RequestType;
import faang.school.accountservice.mapper.RequestMapper;
import faang.school.accountservice.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private RequestService requestService;

    private RequestDto requestDto;
    private Request request;
    private List<Request> requests;

    @BeforeEach
    public void setUp() {
        requestDto = RequestDto.builder().build();
        request = Request.builder().build();
        requests = List.of(
                Request.builder().build(),
                Request.builder().build()
        );
    }

    @Test
    public void testCreateRequest() {
        when(requestMapper.toEntity(requestDto)).thenReturn(request);
        when(requestRepository.save(any())).thenReturn(request);
        when(requestMapper.toDto(any())).thenReturn(requestDto);

        RequestDto result = requestService.createRequest(requestDto);
        assertEquals(requestDto, result);

        verify(requestMapper, times(1)).toEntity(requestDto);
        verify(requestRepository, times(1)).save(any());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<Request> requests = Collections.singletonList(request);

        when(requestRepository.findByUserId(userId)).thenReturn(requests);
        when(requestMapper.toDto(any())).thenReturn(requestDto);

        List<RequestDto> result = requestService.findByUserId(userId);
        assertEquals(1, result.size());

        verify(requestRepository, times(1)).findByUserId(userId);
        verify(requestMapper, times(1)).toDto(any());
    }

    @Test
    public void testFindMyRequests() {
        when(userContext.getUserId()).thenReturn(1L);

        when(requestRepository.findByUserId(1L)).thenReturn(requests);

        List<RequestDto> result = requestService.findMyRequests();
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByRequestStatus() {
        RequestStatus status = RequestStatus.COMPLETED;

        when(requestRepository.findByRequestStatus(status)).thenReturn(requests);

        List<RequestDto> result = requestService.findByRequestStatus(status);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByRequestType() {
        RequestType type = RequestType.AUTHORIZATION;

        when(requestRepository.findByRequestType(type)).thenReturn(requests);

        List<RequestDto> result = requestService.findByRequestType(type);
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateRequest() {
        requestService.updateRequest(requestDto);
        verify(requestRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteRequest() {
        requestService.deleteRequest(requestDto);
        verify(requestRepository, times(1)).delete(any());
    }
}

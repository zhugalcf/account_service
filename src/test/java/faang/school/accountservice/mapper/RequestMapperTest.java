package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.entity.request.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RequestMapperTest {

    @Spy
    private RequestMapperImpl requestMapper;

    @Test
    public void testToEntity() {
        RequestDto requestDto = new RequestDto();
        requestDto.setUserId(1L);

        Request request = requestMapper.toEntity(requestDto);

        assertNotNull(request);
        assertEquals(requestDto.getUserId(), request.getUserId());
    }

    @Test
    public void testToDto() {
        Request request = new Request();
        request.setUserId(1L);

        RequestDto requestDto = requestMapper.toDto(request);

        assertNotNull(requestDto);
        assertEquals(request.getUserId(), requestDto.getUserId());
    }

    @Test
    public void testUpdateRequestFromRequestDto() {
        Request request = new Request();
        request.setUserId(1L);

        RequestDto requestDto = new RequestDto();
        requestDto.setUserId(2L);

        requestMapper.updateRequestFromRequestDto(requestDto, request);

        assertEquals(requestDto.getUserId(), request.getUserId());
    }
}

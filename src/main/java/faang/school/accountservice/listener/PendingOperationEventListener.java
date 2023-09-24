package faang.school.accountservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.PendingOperationDto;
import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.mapper.PendingOperationMapper;
import faang.school.accountservice.service.request.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PendingOperationEventListener extends AbstractEventListener<PendingOperationDto> {

    private final RequestService requestService;
    private final PendingOperationMapper pendingOperationMapper;

    public PendingOperationEventListener(ObjectMapper objectMapper,
                                         RequestService requestService,
                                         PendingOperationMapper pendingOperationMapper) {
        super(objectMapper);
        this.requestService = requestService;
        this.pendingOperationMapper = pendingOperationMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PendingOperationDto operationDto = readValue(message.getBody(), PendingOperationDto.class);
        log.info("Received pending operation {}", operationDto);

        RequestDto requestDto = pendingOperationMapper.toRequestDto(operationDto);
        requestService.createRequest(requestDto);
    }
}

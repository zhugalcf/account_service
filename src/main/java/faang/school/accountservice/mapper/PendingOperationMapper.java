package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.PendingOperationDto;
import faang.school.accountservice.dto.request.RequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PendingOperationMapper {

    @Mapping(target = "requestType", source = "operationType")
    @Mapping(target = "requestStatus", source = "operationStatus")
    @Mapping(target = "inputData", expression = "java(toInputDate(pendingOperationDto))")
    @Mapping(target = "lastModified", source = "updatedAt")
    RequestDto toRequestDto(PendingOperationDto pendingOperationDto);

    default Map<String, Object> toInputDate(PendingOperationDto pendingOperationDto) {
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("amount", pendingOperationDto.getAmount());
        inputData.put("currency", pendingOperationDto.getCurrency());
        inputData.put("senderAccountId", pendingOperationDto.getSenderAccountId());
        inputData.put("receiverAccountId", pendingOperationDto.getReceiverAccountId());
        inputData.put("scheduledAt", pendingOperationDto.getScheduledAt());
        return inputData;
    }
}

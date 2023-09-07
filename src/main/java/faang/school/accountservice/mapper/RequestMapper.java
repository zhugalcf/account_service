package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.dto.request.ExecuteRequestEvent;
import faang.school.accountservice.dto.request.RequestStatusChangedEvent;
import faang.school.accountservice.dto.request.ResponseRequestDto;
import faang.school.accountservice.entity.Request;
import faang.school.accountservice.publisher.ExecuteRequestPublisher;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    Request fromCreateToEntity(CreateRequestDto requestDto);

    ResponseRequestDto toDto(Request request);

    List<ResponseRequestDto> toListDto(List<Request> requestList);

    RequestStatusChangedEvent toStatusChangeEvent(Request request);
    ExecuteRequestEvent toExecuteEvent(Request request);
}

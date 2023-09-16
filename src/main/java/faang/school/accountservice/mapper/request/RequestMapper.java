package faang.school.accountservice.mapper.request;

import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.model.request.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {
    Request toEntity(RequestDto requestDto);

    @Mapping(source = "open", target = "isOpen")
    RequestDto toDto(Request request);

    List<RequestDto> toListDto(List<Request> requestList);
}

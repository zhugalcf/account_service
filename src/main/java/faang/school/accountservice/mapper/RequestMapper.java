package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.dto.request.RequestEvent;
import faang.school.accountservice.entity.Request;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestDto toDto(Request request);
    Request toEntity(RequestDto requestDto);
    RequestEvent toEvent(Request request);
}

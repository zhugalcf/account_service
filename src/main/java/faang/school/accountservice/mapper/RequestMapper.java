package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.model.request.Request;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",injectionStrategy = InjectionStrategy.CONSTRUCTOR,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {
    Request toEntity(RequestDto requestDto);
    RequestDto toDto(Request request);
}

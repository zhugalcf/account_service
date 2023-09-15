package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.request.RequestDto;
import faang.school.accountservice.entity.request.Request;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RequestMapper {
    Request toEntity(RequestDto requestDto);

    RequestDto toDto(Request request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestFromRequestDto(RequestDto requestDto, @MappingTarget Request request);
}

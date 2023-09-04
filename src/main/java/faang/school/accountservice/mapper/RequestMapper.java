package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.ResponseRequestDto;
import faang.school.accountservice.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    ResponseRequestDto toDto(Request request);
    List<ResponseRequestDto> toListDto(List<Request> requestList);
}

package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.ExecuteRequestDto;
import faang.school.accountservice.dto.RequestDto;
import faang.school.accountservice.dto.UpdateRequestDto;
import faang.school.accountservice.model.Request;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RequestMapper {

    Request toEntity(RequestDto requestDto);

    RequestDto toDto(Request request);
    List<RequestDto> entityListToDtoList(List<Request> requestList);

    Request updateToEntity(UpdateRequestDto updateRequestDto);

    UpdateRequestDto toUpdateDto(Request request);

    Request updateEntityFromUpdateDto(UpdateRequestDto updateRequestDto, @MappingTarget Request request);

    ExecuteRequestDto entityToExecuteDto(Request request);
}

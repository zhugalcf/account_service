package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.OwnerDto;
import faang.school.accountservice.model.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OwnerMapper {
    OwnerMapper INSTANCE = Mappers.getMapper(OwnerMapper.class);

    Owner createDtoToEntity(OwnerDto dto);

    OwnerDto entityToResponseDto(Owner entity);
}

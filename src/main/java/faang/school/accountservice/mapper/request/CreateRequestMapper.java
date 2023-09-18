package faang.school.accountservice.mapper.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.request.CreateRequestDto;
import faang.school.accountservice.model.request.Request;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Map;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreateRequestMapper {

    @Mapping(source = "inputData", target = "inputData", qualifiedByName = "objectToMap")
    Request toEntity(CreateRequestDto requestDto);

    CreateRequestDto toDto(Request request);

    @Named("objectToMap")
    default Map<String, Object> objectToMap(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(object, Map.class);
    }
}

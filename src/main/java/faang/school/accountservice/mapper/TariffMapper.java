package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.tariff.TariffDto;
import faang.school.accountservice.model.Tariff;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TariffMapper {
    TariffMapper INSTANCE = Mappers.getMapper(TariffMapper.class);

    TariffDto tariffToTariffDto(Tariff tariff);
}

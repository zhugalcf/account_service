package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.Tariff;
import org.mapstruct.ReportingPolicy;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TariffMapper {

    @Mapping(target = "rates", source = "rate", qualifiedByName = "mapBigDecimalToList")
    Tariff toTariff(TariffDto tariffDto);

    @Mapping(target = "rate", source = "rates", qualifiedByName = "mapListToSingleBigDecimal")
    TariffDto toTariffDto(Tariff tariff);

    @Named("mapListToSingleBigDecimal")
    default BigDecimal mapListToSingleBigDecimal(List<BigDecimal> rates) {
        return rates.get(0);
    }

    @Named("mapBigDecimalToList")
    default List<BigDecimal> mapBigDecimalToList(BigDecimal value) {
        if (value != null) {
            return Collections.singletonList(value);
        }
        return Collections.emptyList();
    }
}
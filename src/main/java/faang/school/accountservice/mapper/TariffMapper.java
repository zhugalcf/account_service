package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.Tariff;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TariffMapper {

    @Mapping(target = "ratePercent", source = "rateHistory", qualifiedByName = "getCurrentRatePercent")
    TariffDto toDto(Tariff tariff);

    Tariff toEntity(TariffDto tariffDto);

    @Named("getCurrentRatePercent")
    default BigDecimal getCurrentRatePercent(List<Rate> rateList) {
        Rate currentRate = rateList.get(rateList.size() - 1);
        return currentRate.getPercent();
    }

    @Named("getCurrentTariff")
    default TariffDto getCurrentTariff(List<Tariff> tariffList) {
        Tariff tariff = tariffList.get(tariffList.size() - 1);
        return toDto(tariff);
    }
}
package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TariffMapper {

    @Mapping(target = "ratePercent", source = "rateHistory", qualifiedByName = "getCurrentRatePercent")
    TariffDto toDto(Tariff tariff);

    Tariff toEntity(TariffDto tariffDto);

    @Named("getCurrentRatePercent")
    default float getCurrentRatePercent(List<Rate> rateList) {
        return rateList.get(rateList.size() - 1)
                .getPercent();
    }

    @Named("getCurrentTariff")
    default TariffDto getCurrentTariff(List<TariffHistory> tariffList) {
        Tariff tariff = tariffList.get(tariffList.size() - 1).getTariff();
        return toDto(tariff);
    }
}
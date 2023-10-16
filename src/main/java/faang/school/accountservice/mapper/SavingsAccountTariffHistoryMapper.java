package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountTariffHistoryDto;
import faang.school.accountservice.entity.SavingsAccountTariffHistory;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingsAccountTariffHistoryMapper {

    @Mapping(source = "savingsAccount.id", target = "savingsAccountId")
    @Mapping(source = "tariff.id", target = "tariffId")
    SavingsAccountTariffHistoryDto toDto(SavingsAccountTariffHistory entity);

    @Mapping(source = "savingsAccountId", target = "savingsAccount.id")
    @Mapping(source = "tariffId", target = "tariff.id")
    SavingsAccountTariffHistory toEntity(SavingsAccountTariffHistoryDto dto);
}
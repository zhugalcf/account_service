package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.model.saving.SavingAccount;
import faang.school.accountservice.model.saving.Tariff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    @Mapping(source = "current_tariff", target = "current_tariff", ignore = true)
    SavingAccount toEntity(SavingsAccountDto dto);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "current_tariff", source = "current_tariff.id")
    SavingsAccountDto toDto(SavingAccount entity);

    TariffDto toDto(Tariff entity);

    Tariff toEntity(TariffDto dto);
}

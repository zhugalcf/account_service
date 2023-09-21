package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountResponseDto;
import faang.school.accountservice.model.SavingsAccount;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = TariffMapper.class)
public interface SavingsAccountResponseMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "tariffDto", source = "tariffHistory", qualifiedByName = "getCurrentTariff")
    SavingsAccountResponseDto toDto(SavingsAccount savingsAccount);

}

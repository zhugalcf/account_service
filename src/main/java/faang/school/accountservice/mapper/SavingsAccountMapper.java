package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountCreateDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.TariffHistory;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SavingsAccountMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "tariffType", source = "tariffHistory", qualifiedByName = "getCurrentTariff")
    SavingsAccountCreateDto toDto(SavingsAccount savingsAccount);

    SavingsAccount toEntity(SavingsAccountCreateDto savingsAccountCreateDto);

    @Named("getCurrentTariff")
    default TariffType getCurrentTariff(List<TariffHistory> tariffHistoryList) {
        return tariffHistoryList.get(tariffHistoryList.size() - 1).getTariff().getType();
    }
}

package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.model.SavingsAccount;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SavingsAccountMapper {

    @Mapping(target = "accountId", source = "account.id")
    SavingsAccountDto toDto(SavingsAccount savingsAccount);

    SavingsAccount toEntity(SavingsAccountDto savingsAccountDto);
}

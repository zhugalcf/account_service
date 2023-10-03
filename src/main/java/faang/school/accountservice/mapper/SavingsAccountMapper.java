package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.entity.SavingsAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingsAccountMapper {

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "tariff.id", target = "tariffId")
    SavingsAccountDto toDto(SavingsAccount savingsAccount);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "tariffId", target = "tariff.id")
    SavingsAccount toEntity(SavingsAccountDto savingsAccountDto);
}

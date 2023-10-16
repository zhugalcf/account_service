package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingsAccountMapper {

    @Mapping(source = "accountId", target = "account.id")
    SavingsAccount toEntity(SavingsAccountDto savingsAccountDto);

    @Mapping(source = "account.id", target = "accountId")
    SavingsAccountDto toDto(SavingsAccount savingsAccount);
}
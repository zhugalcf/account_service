package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingsAccountMapper {

    @Mapping(target = "accountId", source = "account", qualifiedByName = "mapAccount")
    SavingsAccount toEntity(SavingsAccountDto savingsAccountDto, @Context Long accountId);

    @Mapping(source = "account.id", target = "accountId")
    SavingsAccountDto toDto(SavingsAccount savingsAccount);

    @AfterMapping
    @Named("mapAccount")
    default Account mapAccount(Long accountId) {
        return Account.builder().id(accountId).build();
    }
}
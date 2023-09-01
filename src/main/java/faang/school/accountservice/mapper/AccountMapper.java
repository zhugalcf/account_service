package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    AccountDto toDto(Account account);

    @Mapping(source = "ownerId", target = "owner.id")
    Account toEntity(AccountDto accountDto);
}


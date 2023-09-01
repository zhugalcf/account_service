package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "typeOfOwner" , source = "ownerType")
    AccountDto accountToAccountDto(Account account);

    Account accountDtoToAccount(AccountDto accountDto);
}

package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.AccountRequestDto;
import faang.school.accountservice.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AccountRequestMapper {

    AccountRequestDto accountToAccountDto(Account account);

    Account accountDtoToAccount(AccountRequestDto accountDto);
}

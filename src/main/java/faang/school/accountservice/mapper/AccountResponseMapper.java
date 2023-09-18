package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.AccountResponseDto;
import faang.school.accountservice.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AccountResponseMapper {

    AccountResponseDto accountToResponseDto(Account account);

    Account responseDtoToAccount(AccountResponseDto answerDto);

}

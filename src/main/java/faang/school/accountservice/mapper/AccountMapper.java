package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.account.CreateAccountDto;
import faang.school.accountservice.dto.account.ResponseAccountDto;
import faang.school.accountservice.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = OwnerMapper.class,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account createDtoToEntity(CreateAccountDto dto);

    ResponseAccountDto entityToResponseDto(Account entity);
}

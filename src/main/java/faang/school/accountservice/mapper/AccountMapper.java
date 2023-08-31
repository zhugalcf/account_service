package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.account.AccountClosingEvent;
import faang.school.accountservice.dto.account.AccountCreationEvent;
import faang.school.accountservice.dto.account.AccountDto;
import faang.school.accountservice.dto.account.AccountFreezingEvent;
import faang.school.accountservice.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    AccountDto toDto(Account account);

    @Mapping(target = "status", expression = "java(faang.school.accountservice.model.AccountStatus.ACTIVE)")
    Account toEntity(AccountDto accountDto);

    AccountCreationEvent toAccountCreateEvent(Account account);

    AccountFreezingEvent toAccountFreezingEvent(Account account);

    AccountClosingEvent toAccountClosingEvent(Account account);
}

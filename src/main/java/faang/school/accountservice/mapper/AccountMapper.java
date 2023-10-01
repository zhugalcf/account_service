package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.owner.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    @Named("mapOwnerId")
    default Long mapOwnerId(Owner owner) {
        return owner.getId();
    }
   @Mapping(source = "owner", target = "id", qualifiedByName = "mapOwnerId")
    AccountDto toDto(Account account);
    Account toEntity(AccountDto accountDto);
}

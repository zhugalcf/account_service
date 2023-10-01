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
//    @Named("map")
//    default Long map(Owner owner) {
//        return owner.getId();
//    }
//   @Mapping(source = "owner", target = "id", qualifiedByName = "map")
//    AccountDto toDto(Account account);
@Mapping(source = "owner", target = "owner.id")
Account toEntity(AccountDto accountDto);

    @Mapping(source = "owner.id", target = "owner")
    AccountDto toDto(Account account);
}

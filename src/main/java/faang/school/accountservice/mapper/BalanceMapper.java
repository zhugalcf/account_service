package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceMapper {
    @Named("mapAccountNumber")
    default Long mapAccountId(Account account) {
        return account.getId();
    }
    @Mapping(source = "account", target = "accountNumber", qualifiedByName = "mapAccountNumber")
    BalanceDto toDto(Balance balance);
    Balance toEntity(BalanceDto balanceDto);
}

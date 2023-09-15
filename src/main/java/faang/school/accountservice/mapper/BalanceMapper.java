package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceMapper {

    @Mapping(target = "accountNumber", source = "accountNumber", qualifiedByName = "toAccount")
    Balance toModel(BalanceDto dto);

    @Mapping(target = "accountNumber", source = "balance.accountNumber.number")
    BalanceDto toDto(Balance balance);

    @Named("toAccount")
    default Account toAccount(String accountNumber) {
        return Account.builder()
                .number(accountNumber)
                .build();
    }
}

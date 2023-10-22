package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.BalanceAudit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = OwnerMapper.class,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceAuditMapper {
    BalanceAuditMapper INSTANCE = Mappers.getMapper(BalanceAuditMapper.class);

    @Mapping(source = "balance.account", target = "account")
    @Mapping(source = "balance.version", target = "balanceVersion")
    BalanceAudit toBalanceAudit(Balance balance);

    @Mapping(source = "account.id", target = "accountId")
    BalanceAuditDto toDto(BalanceAudit balanceAudit);

    default List<BalanceAuditDto> toDtoList(List<BalanceAudit> audits) {
        return audits.stream().map(this::toDto).toList();
    };
}
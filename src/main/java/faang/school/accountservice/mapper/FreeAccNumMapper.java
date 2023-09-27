package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.account.FreeAccountNumberDto;
import faang.school.accountservice.model.FreeAccountNumber;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = OwnerMapper.class,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FreeAccNumMapper {
    FreeAccountNumberDto toDto (Optional<FreeAccountNumber> number);
    FreeAccountNumber toEntity(FreeAccountNumberDto freeAccountNumberDto);
}

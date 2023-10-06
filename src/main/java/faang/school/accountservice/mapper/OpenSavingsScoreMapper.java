package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.OpenSavingsScoreDto;
import faang.school.accountservice.model.SavingsAccount;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OpenSavingsScoreMapper {
    OpenSavingsScoreMapper INSTANCE = Mappers.getMapper(OpenSavingsScoreMapper.class);

    OpenSavingsScoreDto toOpenSavingsScoreDto(SavingsAccount savingsAccount);
}

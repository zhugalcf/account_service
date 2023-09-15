package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.CurrencyDto;
import faang.school.accountservice.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrencyMapper {

    CurrencyDto toDto(Currency currency);

    Currency toEntity(CurrencyDto currencyDto);
}

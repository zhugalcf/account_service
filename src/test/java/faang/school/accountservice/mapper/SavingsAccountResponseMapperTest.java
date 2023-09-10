package faang.school.accountservice.mapper;


import faang.school.accountservice.dto.SavingsAccountResponseDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SavingsAccountResponseMapperTest {

    private final TariffMapper tariffMapper = new TariffMapperImpl();
    private final SavingsAccountResponseMapper savingsAccountResponseMapper = new SavingsAccountResponseMapperImpl(tariffMapper);

    @Test
    void toDtoTest() {
        LocalDateTime lastInterestDate = LocalDateTime.now().minusDays(5);
        LocalDateTime createdAt = LocalDateTime.now().minusMonths(6);
        LocalDateTime updatedAt = LocalDateTime.now().minusMonths(3);
        BigDecimal firstDecimal = new BigDecimal(5);
        BigDecimal secondDecimal = new BigDecimal(10);

        Rate firstRate = Rate.builder()
                .id(10)
                .percent(firstDecimal)
                .build();
        Rate secondRate = Rate.builder()
                .id(11)
                .percent(secondDecimal)
                .build();

        Tariff first = Tariff.builder()
                .id(5)
                .type(TariffType.BUSINESS)
                .rateHistory(List.of())
                .build();

        Tariff second = Tariff.builder()
                .id(6)
                .type(TariffType.BASIC)
                .rateHistory(List.of(firstRate, secondRate))
                .build();

        Account account = Account.builder().id(1).build();

        SavingsAccount savingsAccount = SavingsAccount.builder()
                .id(1)
                .account(account)
                .tariffHistory(List.of(first, second))
                .lastInterestCalculateDate(lastInterestDate)
                .version(0)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        TariffDto tariffDto = TariffDto.builder()
                .id(6)
                .type(TariffType.BASIC)
                .ratePercent(secondDecimal)
                .build();

        SavingsAccountResponseDto expected = SavingsAccountResponseDto.builder()
                .id(1)
                .accountId(1)
                .tariffDto(tariffDto)
                .version(0)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        SavingsAccountResponseDto result = savingsAccountResponseMapper.toDto(savingsAccount);

        assertEquals(expected, result);
    }
}
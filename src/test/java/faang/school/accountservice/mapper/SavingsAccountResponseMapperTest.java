package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountResponseDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Rate;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SavingsAccountResponseMapperTest {

    private TariffMapper tariffMapper = new TariffMapperImpl();

    private SavingsAccountResponseMapper mapper = new SavingsAccountResponseMapperImpl(tariffMapper);

    private SavingsAccount savingsAccount;
    private Account account;
    private TariffHistory tariffHistory;
    private Tariff tariff;
    private Rate rate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private final String accountNumber = "4159000000000001";
    private final BigDecimal balance = BigDecimal.valueOf(1157.12);

    @BeforeEach
    void setUp() {
        createdAt = LocalDateTime.now().minusMonths(1);
        updatedAt = LocalDateTime.now().minusDays(1);
        rate = Rate.builder()
                .percent(10.5f)
                .build();
        tariff = Tariff.builder()
                .id(10)
                .type(TariffType.PROMO)
                .rateHistory(new ArrayList<>(List.of(rate)))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        tariffHistory = TariffHistory.builder()
                .tariff(tariff)
                .build();
        account = Account.builder()
                .id(5)
                .build();
        savingsAccount = SavingsAccount.builder()
                .id(1)
                .accountNumber(accountNumber)
                .account(account)
                .balance(balance)
                .tariffHistory(new ArrayList<>(List.of(tariffHistory)))
                .version(1)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Test
    void toDtoTest() {
        TariffDto tariffDto = TariffDto.builder()
                .id(10)
                .type(TariffType.PROMO)
                .ratePercent(10.5f)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        SavingsAccountResponseDto expected = SavingsAccountResponseDto.builder()
                .id(1)
                .accountNumber(accountNumber)
                .accountId(5)
                .balance(balance)
                .tariffDto(tariffDto)
                .version(1)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        SavingsAccountResponseDto result = mapper.toDto(savingsAccount);

        assertEquals(expected, result);
    }
}
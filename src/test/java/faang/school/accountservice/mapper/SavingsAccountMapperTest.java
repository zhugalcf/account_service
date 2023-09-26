package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountCreateDto;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SavingsAccountMapperTest {

    private SavingsAccountMapper mapper = new SavingsAccountMapperImpl();

    private SavingsAccountCreateDto createDto;
    private SavingsAccount savingsAccount;
    private Account account;
    private TariffHistory tariffHistory;
    private Tariff tariff;

    @BeforeEach
    void setUp() {

        tariff = Tariff.builder()
                .type(TariffType.BASIC)
                .build();
        tariffHistory = TariffHistory.builder()
                .tariff(tariff)
                .build();
        account = Account.builder()
                .id(5)
                .build();
        savingsAccount = SavingsAccount.builder()
                .account(account)
                .tariffHistory(new ArrayList<>(List.of(tariffHistory)))
                .build();
        createDto = SavingsAccountCreateDto.builder()
                .accountId(5L)
                .tariffType(TariffType.BASIC)
                .build();

    }

    @Test
    void toDtoTest() {
        SavingsAccountCreateDto result = mapper.toDto(savingsAccount);

        assertEquals(createDto, result);
    }

    @Test
    void toEntityTest() {
        SavingsAccount expected = SavingsAccount.builder().build();

        SavingsAccount result = mapper.toEntity(createDto);

        assertEquals(expected, result);
    }
}
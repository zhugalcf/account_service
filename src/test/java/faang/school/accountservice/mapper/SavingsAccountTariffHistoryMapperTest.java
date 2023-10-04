package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountTariffHistoryDto;
import faang.school.accountservice.entity.SavingsAccountTariffHistory;
import faang.school.accountservice.entity.Tariff;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SavingsAccountTariffHistoryMapperTest {
    @Spy
    private SavingsAccountTariffHistoryMapperImpl mapper;

    @Test
    public void testToDto() {
        SavingsAccountTariffHistory entity = new SavingsAccountTariffHistory();
        entity.setId(1L);

        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setId(10L);

        Tariff tariff = new Tariff();
        tariff.setId(100L);

        entity.setSavingsAccount(savingsAccount);
        entity.setTariff(tariff);

        SavingsAccountTariffHistoryDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(savingsAccount.getId(), dto.getSavingsAccountId());
        assertEquals(tariff.getId(), dto.getTariffId());
    }

    @Test
    public void testToEntity() {
        SavingsAccountTariffHistoryDto dto = new SavingsAccountTariffHistoryDto();
        dto.setId(1L);
        dto.setSavingsAccountId(10L);
        dto.setTariffId(100L);

        SavingsAccountTariffHistory entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getSavingsAccountId(), entity.getSavingsAccount().getId());
        assertEquals(dto.getTariffId(), entity.getTariff().getId());
    }
}
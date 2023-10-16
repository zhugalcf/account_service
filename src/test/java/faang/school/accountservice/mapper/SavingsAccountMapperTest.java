package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SavingsAccountMapperTest {
    @Spy
    private SavingsAccountMapperImpl savingsAccountMapper;
    private SavingsAccount savingsAccount;
    private SavingsAccountDto dto;

    @Test
    public void testToEntity() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0);

        dto = SavingsAccountDto.builder()
                .id(1L)
                .accountId(101L)
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .closedAt(null)
                .lastUpdateCalculationAt(dateTime)
                .build();

        SavingsAccount entity = savingsAccountMapper.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getAccountId(), entity.getAccount().getId());
        assertEquals(dto.getAccountType(), entity.getAccountType());
        assertEquals(dto.getAccountStatus(), entity.getAccountStatus());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
        assertEquals(dto.getClosedAt(), entity.getClosedAt());
        assertEquals(dto.getLastUpdateCalculationAt(), entity.getLastUpdateCalculationAt());
    }

    @Test
    public void testEntityToDtoMapping() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0);

        savingsAccount = SavingsAccount.builder()
                .id(1L)
                .account(Account.builder().id(101L).build())
                .tariffHistoryIds(Collections.singletonList(1L))
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .closedAt(null)
                .lastUpdateCalculationAt(dateTime)
                .version(1L)
                .build();

        SavingsAccountDto dto = savingsAccountMapper.toDto(savingsAccount);

        assertEquals(savingsAccount.getId(), dto.getId());
        assertEquals(savingsAccount.getAccount().getId(), dto.getAccountId());
        assertEquals(savingsAccount.getAccountType(), dto.getAccountType());
        assertEquals(savingsAccount.getAccountStatus(), dto.getAccountStatus());
        assertEquals(savingsAccount.getCreatedAt(), dto.getCreatedAt());
        assertEquals(savingsAccount.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(savingsAccount.getClosedAt(), dto.getClosedAt());
        assertEquals(savingsAccount.getLastUpdateCalculationAt(), dto.getLastUpdateCalculationAt());
    }
}
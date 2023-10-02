package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SavingsAccountMapperTest {
    @Spy
    private SavingsAccountMapperImpl savingsAccountMapper;

    @Spy
    private AccountMapperImpl accountMapper;
    private SavingsAccount expectedSavingsAccount;
    private SavingsAccountDto expectedSavingsAccountDto;

    @BeforeEach
    void setUp() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0);

        expectedSavingsAccount = SavingsAccount.builder()
                .id(1L)
                .account(Account.builder().id(2L).build())
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .closedAt(null)
                .lastUpdateCalculationAt(null)
                .build();

        expectedSavingsAccountDto = SavingsAccountDto.builder()
                .id(1L)
                .accountId(2L)
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .closedAt(null)
                .lastUpdateCalculationAt(null)
                .build();
    }

    @Test
    void testToDto() {
        SavingsAccountDto actual = savingsAccountMapper.toDto(expectedSavingsAccount);

        assertEquals(expectedSavingsAccountDto, actual);
    }

    @Test
    void testToEntity() {
        SavingsAccount actual = savingsAccountMapper.toEntity(expectedSavingsAccountDto);

        assertEquals(expectedSavingsAccount, actual);
    }
}
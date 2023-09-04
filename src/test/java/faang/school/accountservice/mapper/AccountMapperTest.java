package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AccountMapperTest {
    @Spy
    private AccountMapperImpl accountMapper;
    private Account accountExpected;
    private AccountDto accountDtoExpected;

    @BeforeEach
    void setUp() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        accountExpected = Account.builder()
                .id(1L)
                .accountNumber("123")
                .owner(Owner.builder().id(1L).build())
                .accountType(AccountType.CHECKING_ACCOUNT)
                .currency(Currency.USD)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .build();

        accountDtoExpected = AccountDto.builder()
                .id(1L)
                .accountNumber("123")
                .ownerId(1L)
                .accountType(AccountType.CHECKING_ACCOUNT)
                .currency(Currency.USD)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .build();
    }

    @Test
    void toDto_shouldMatchByEquals() {
        AccountDto actual = accountMapper.toDto(accountExpected);
        assertEquals(accountDtoExpected, actual);
    }

    @Test
    void toEntity() {
        Account actual = accountMapper.toEntity(accountDtoExpected);
        actual.setOwner(Owner.builder().id(1L).build());
        assertEquals(accountExpected, actual);
    }
}
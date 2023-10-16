package faang.school.accountservice.service;

import static org.junit.jupiter.api.Assertions.*;
import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.dto.SavingsAccountTariffHistoryDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.entity.SavingsAccountTariffHistory;
import faang.school.accountservice.entity.Tariff;
import faang.school.accountservice.entity.TariffType;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.Currency;
import faang.school.accountservice.entity.account.SavingsAccount;
import faang.school.accountservice.exception.EntityNotFoundException;
import faang.school.accountservice.exception.TariffNotFoundException;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.mapper.SavingsAccountTariffHistoryMapper;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.SavingsAccountTariffHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SavingsAccountServiceTest {
    @InjectMocks
    private SavingsAccountService savingsAccountService;

    @Mock
    private OwnerService ownerService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private AccountService accountService;

    @Mock
    private SavingsAccountRepository savingsAccountRepository;

    @Mock
    private SavingsAccountTariffHistoryRepository savingsAccountTariffHistoryRepository;

    @Mock
    private UniqueNumberService uniqueNumberService;

    @Mock
    private SavingsAccountMapper savingsAccountMapper;

    @Mock
    private SavingsAccountTariffHistoryMapper savingsAccountTariffHistoryMapper;

    private SavingsAccount savingsAccount;
    private SavingsAccountDto dto;
    private Tariff tariff;
    private TariffDto tariffDto;
    private SavingsAccountTariffHistory tariffHistory;
    private SavingsAccountTariffHistoryDto tariffHistoryDto;
    private Account account;
    private Owner owner;
    private String savingsAccountNumber;

    @BeforeEach
    void setUp() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        savingsAccountNumber = "42550000000000000001";
        String currencyCode = "USD";

        owner = Owner.builder()
                .id(1L)
                .build();

        account = Account.builder()
                .id(1L)
                .owner(owner)
                .currency(Currency.builder().code(currencyCode).build())
                .build();

        savingsAccount = SavingsAccount.builder()
                .id(1L)
                .account(account)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .build();

        dto = SavingsAccountDto.builder()
                .id(1L)
                .accountId(account.getId())
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(account.getAccountStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();

        tariff = Tariff.builder()
                .id(1L)
                .tariffType(TariffType.BASE)
                .rates(List.of(new BigDecimal("0.5")))
                .build();

        tariffDto = TariffDto.builder()
                .id(1L)
                .tariffType(TariffType.BASE)
                .rate(new BigDecimal("0.5"))
                .build();

        tariffHistory = SavingsAccountTariffHistory.builder()
                .id(1L)
                .savingsAccount(savingsAccount)
                .tariff(tariff)
                .changeDate(dateTime)
                .build();

        tariffHistoryDto = SavingsAccountTariffHistoryDto.builder()
                .id(1L)
                .savingsAccountId(savingsAccount.getId())
                .tariffId(tariff.getId())
                .changeDate(dateTime)
                .build();

        when(accountService.getAccount(2L)).thenReturn(null);
        when(accountService.getAccount(3L)).thenReturn(Account.builder().accountType(AccountType.CHECKING_ACCOUNT).build());

        when(ownerService.getOwner(1L)).thenReturn(owner);
        when(currencyService.getCurrency(currencyCode)).thenReturn(Currency.builder().code(currencyCode).build());
        when(uniqueNumberService.getFreeAccountNumber(AccountType.SAVINGS_ACCOUNT)).thenReturn(savingsAccountNumber);

        when(savingsAccountRepository.findById(1L)).thenReturn(Optional.of(savingsAccount));
        when(savingsAccountRepository.findByAccount_Id(1L)).thenReturn(savingsAccount);
        when(savingsAccountRepository.save(savingsAccount)).thenReturn(savingsAccount);
        when(savingsAccountMapper.toDto(savingsAccount)).thenReturn(dto);
        when(savingsAccountMapper.toEntity(dto)).thenReturn(savingsAccount);
        when(savingsAccountService.getSavingsAccountByAccountId(1L)).thenReturn(savingsAccount);

        when(savingsAccountTariffHistoryRepository.findTopBySavingsAccountOrderByChangeDateDesc(savingsAccount)).thenReturn(tariffHistory);
        when(savingsAccountTariffHistoryRepository.findAll()).thenReturn(List.of());
        when(savingsAccountTariffHistoryMapper.toDto(tariffHistory)).thenReturn(tariffHistoryDto);
    }


    @Test
    void getSavingsAccountIdByAccountId_WhenSavingsAccountExists() {
        Long accountId = 1L;
        Account savingsAccount = new Account();
        savingsAccount.setAccountType(AccountType.SAVINGS_ACCOUNT);

        when(accountService.getAccount(accountId)).thenReturn(savingsAccount);

        Long savingsAccountId = savingsAccountService.getSavingsAccountIdByAccountId(accountId);

        assertEquals(accountId, savingsAccountId);
    }

    @Test
    void getSavingsAccountIdByAccountId_WhenSavingsAccountDoesNotExist() {
        long accountId = 1L;
        Account checkingAccount = new Account();
        checkingAccount.setAccountType(AccountType.CHECKING_ACCOUNT);

        when(accountService.getAccount(accountId)).thenReturn(checkingAccount);

        assertThrows(EntityNotFoundException.class, () -> savingsAccountService.getSavingsAccountIdByAccountId(accountId));
    }

    @Test
    void testGetSavingsAccountById() {
        SavingsAccountDto resultDto = savingsAccountService.get(1L);
        assertEquals(dto, resultDto);
    }

    @Test
    void testGetSavingsAccountByIdNotFound() {
        when(savingsAccountRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            savingsAccountService.get(2L);
        });
    }

    @Test
    void testGetCurrentTariffAndRateByClientId() {
        TariffDto resultDto = savingsAccountService.getCurrentTariffAndRateByClientId(1L);
        assertEquals(tariffDto, resultDto);
    }

    @Test
    void testGetCurrentTariffAndRateByClientIdNoTariffHistory() {
        when(savingsAccountTariffHistoryRepository.findTopBySavingsAccountOrderByChangeDateDesc(savingsAccount)).thenReturn(null);

        assertThrows(TariffNotFoundException.class, () -> {
            savingsAccountService.getCurrentTariffAndRateByClientId(1L);
        });
    }

    @Test
    void testGetCurrentTariffAndRateByClientIdNoSavingsAccount() {
        when(savingsAccountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            savingsAccountService.getCurrentTariffAndRateByClientId(1L);
        });
    }

    @Test
    void testGetSavingsAccountByAccountId() {
        SavingsAccount resultAccount = savingsAccountService.getSavingsAccountByAccountId(1L);
        assertEquals(savingsAccount, resultAccount);
    }

    @Test
    void testGetSavingsAccountByAccountIdNotFound() {
        when(savingsAccountRepository.findByAccount_Id(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            savingsAccountService.getSavingsAccountByAccountId(1L);
        });
    }

    @Test
    void getSavingsAccountIdByAccountId_WhenAccountIsSavingsAccount() {
        when(accountService.getAccount(1L)).thenReturn(Account.builder().accountType(AccountType.SAVINGS_ACCOUNT).build());

        assertDoesNotThrow(() -> {
            savingsAccountService.getSavingsAccountIdByAccountId(1L);
        });
    }

    @Test
    void getSavingsAccountIdByAccountId_WhenAccountIsNull() {
        when(accountService.getAccount(2L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            savingsAccountService.getSavingsAccountIdByAccountId(2L);
        });
    }

    @Test
    void getSavingsAccountIdByAccountId_WhenAccountIsNotSavingsAccount() {
        when(accountService.getAccount(3L)).thenReturn(Account.builder().accountType(AccountType.CHECKING_ACCOUNT).build());

        assertThrows(EntityNotFoundException.class, () -> {
            savingsAccountService.getSavingsAccountIdByAccountId(3L);
        });
    }
}
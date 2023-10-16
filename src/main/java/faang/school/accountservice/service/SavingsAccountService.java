package faang.school.accountservice.service;

import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.entity.SavingsAccountTariffHistory;
import faang.school.accountservice.entity.Tariff;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.Currency;
import faang.school.accountservice.entity.account.SavingsAccount;
import faang.school.accountservice.exception.EntityNotFoundException;
import faang.school.accountservice.exception.TariffNotFoundException;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.SavingsAccountTariffHistoryRepository;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import faang.school.accountservice.dto.SavingsAccountDto;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsAccountService {
    private final OwnerService ownerService;
    private final CurrencyService currencyService;
    private final AccountService accountService;
    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountRepository accountRepository;
    private final SavingsAccountTariffHistoryRepository savingsAccountTariffHistoryRepository;
    private final UniqueNumberService uniqueNumberService;
    private final SavingsAccountMapper savingsAccountMapper;


    @Transactional
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public SavingsAccountDto openSavingsAccount(Long ownerId, String currencyCode) {
        Owner owner = getOwner(ownerId);
        Currency currency = getCurrency(currencyCode);

        String accountNumber = uniqueNumberService.getFreeAccountNumber(AccountType.SAVINGS_ACCOUNT);

        Account account = createAccount(owner, currency, accountNumber);

        SavingsAccountDto savingsAccountDto = SavingsAccountDto.builder()
                .accountId(account.getId())
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.OPENED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return savingsAccountMapper.toDto(savingsAccountRepository.save(savingsAccountMapper.toEntity(savingsAccountDto)));
    }
    private Owner getOwner(Long ownerId) {
        return ownerService.getOwner(ownerId);
    }

    private Currency getCurrency(String currencyCode) {
        return currencyService.getCurrency(currencyCode);
    }
    private Account createAccount(Owner owner, Currency currency, String accountNumber) {
        Account account = Account.builder()
                .owner(owner)
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .currency(currency)
                .accountStatus(AccountStatus.OPENED)
                .accountNumber(accountNumber)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public SavingsAccountDto get(Long id) {
        SavingsAccount savingsAccount = getSavingsAccount(id);
        return savingsAccountMapper.toDto(savingsAccount);
    }

    @Transactional(readOnly = true)
    public TariffDto getCurrentTariffAndRateByClientId(Long clientId) {
        SavingsAccount savingsAccount = savingsAccountRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Savings account not found"));

        SavingsAccountTariffHistory latestTariffHistory =
                savingsAccountTariffHistoryRepository.findTopBySavingsAccountOrderByChangeDateDesc(savingsAccount);

        if (latestTariffHistory == null) {
            throw new TariffNotFoundException("Tariff history is not available or has not been created yet");
        } else {
            Tariff tariff = latestTariffHistory.getTariff();

            TariffDto tariffAndRateDto = new TariffDto();
            tariffAndRateDto.setId(tariff.getId());
            tariffAndRateDto.setTariffType(tariff.getTariffType());
            tariffAndRateDto.setRate(tariff.getRates().get(0));

            return tariffAndRateDto;
        }
    }

    public SavingsAccount getSavingsAccountByAccountId(Long accountId) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByAccount_Id(accountId);
        if (savingsAccount == null) {
            throw new EntityNotFoundException("Savings account not found for accountId: " + accountId);
        }
        return savingsAccount;
    }

    @Transactional(readOnly = true)
    public Long getSavingsAccountIdByAccountId(Long accountId) {
        Account account = accountService.getAccount(accountId);

        if (account != null && account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
            getSavingsAccountByAccountId(accountId);
        } else {
            throw new EntityNotFoundException("Account with accountId " + accountId + " is not a savings account");
        }
        return accountId;
    }

    public SavingsAccount getSavingsAccount(long id) {
        return savingsAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Savings account with id " + id + " not found"));
    }
}
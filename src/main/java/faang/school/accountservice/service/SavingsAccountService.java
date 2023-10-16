package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountCreateDto;
import faang.school.accountservice.dto.SavingsAccountResponseDto;
import faang.school.accountservice.dto.SavingsAccountUpdateDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.exception.AccountAlreadyExistException;
import faang.school.accountservice.exception.DataValidationException;
import faang.school.accountservice.exception.InvalidStatusException;
import faang.school.accountservice.exception.InvalidTypeException;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.exception.TariffAlreadyAssignedException;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.mapper.SavingsAccountResponseMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.repository.SavingsAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountService accountService;
    private final TariffService tariffService;
    private final SavingsAccountMapper savingsAccountMapper;
    private final SavingsAccountResponseMapper savingsAccountResponseMapper;
    private final FreeAccountNumbersService freeAccountNumbersService;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${scheduler.savings-account.rate-calculation.batch-size}")
    private int batchSize;

    @Transactional
    public SavingsAccountResponseDto openSavingsAccount(SavingsAccountCreateDto savingsAccountCreateDto) {
        long accountId = savingsAccountCreateDto.getAccountId();
        TariffType tariffType = savingsAccountCreateDto.getTariffType();
        Account account = accountService.getAccountById(accountId);

        checkSavingsAccountAlreadyExist(account);
        validateAccountTypeAndStatus(account);
        log.info("Account with ID: {}, successfully passed all the necessary validations.", accountId);

        SavingsAccount accountToSave = savingsAccountMapper.toEntity(savingsAccountCreateDto);
        accountToSave.setAccountNumber(freeAccountNumbersService.getNumber(AccountType.SAVINGS, (t, n) -> log.info("Received request to obtain an unique account number for account type '{}'", t)));
        accountToSave.setBalance(BigDecimal.ZERO);
        accountToSave.setAccount(account);
        accountToSave.setVersion(1);

        savingsAccountRepository.save(accountToSave);
        log.info("Savings account for account with ID: {}, has been successfully saved", accountId);

        TariffHistory currentTariff = tariffService.assignTariffToSavingsAccount(accountToSave, tariffType);
        log.info("Tariff with type: {}, has been successfully assigned to savings account", tariffType);

        accountToSave.setTariffHistory(new ArrayList<>(List.of(currentTariff)));

        return savingsAccountResponseMapper.toDto(accountToSave);
    }

    @Transactional
    public SavingsAccountResponseDto changeSavingsAccountTariff(SavingsAccountUpdateDto updateDto) {
        long savingsAccountId = updateDto.getSavingsAccountId();
        TariffType newTariffType = Objects.requireNonNull(updateDto.getTariffType(), "Tariff type cant be null");

        SavingsAccount savingsAccount = getSavingsAccountBy(savingsAccountId);
        TariffType currentTariffType = getCurrentSavingsAccountTariff(savingsAccount).getType();

        if (currentTariffType != newTariffType) {
            TariffHistory tariffHistory = tariffService.assignTariffToSavingsAccount(savingsAccount, newTariffType);
            log.info("Tariff with type: {}, has been successfully assigned to savings account", newTariffType);

            savingsAccount.getTariffHistory().add(tariffHistory);
            savingsAccount.setVersion(savingsAccount.getVersion() + 1);
            savingsAccount.setUpdatedAt(LocalDateTime.now());

            return savingsAccountResponseMapper.toDto(savingsAccount);
        }
        throw new TariffAlreadyAssignedException(String.format("Tariff: %s, already assigned to saving account with id: %d", newTariffType, savingsAccountId));
    }

    @Transactional
    public SavingsAccountResponseDto addFundsToSavingsAccount(SavingsAccountUpdateDto updateDto) {
        long savingsAccountId = updateDto.getSavingsAccountId();
        BigDecimal funds = Objects.requireNonNull(updateDto.getMoneyAmount(), "You must specify the amount you want to deposit into your savings account");

        validateRequestMoneyAmount(funds);
        SavingsAccount savingsAccount = getSavingsAccountBy(savingsAccountId);

        BigDecimal oldBalance = savingsAccount.getBalance();
        BigDecimal newBalance = oldBalance.add(funds);
        savingsAccount.setBalance(newBalance);
        savingsAccount.setVersion(savingsAccount.getVersion() + 1);

        log.info("Savings account balance where updated from '{}', to '{}'", oldBalance, newBalance);
        return savingsAccountResponseMapper.toDto(savingsAccount);
    }

    public SavingsAccountResponseDto getSavingsAccountDtoBy(long id) {
        return savingsAccountResponseMapper.toDto(getSavingsAccountBy(id));
    }

    public SavingsAccountResponseDto getSavingsAccountByOwner(long ownerId, OwnerType ownerType) {
        Account account = accountService.findAccountByOwnerIdAndOwnerType(ownerId, ownerType);
        checkSavingsAccountDoesNotExist(account);
        return savingsAccountResponseMapper.toDto(account.getSavingsAccount());
    }

    public SavingsAccount getSavingsAccountBy(long id) {
        return savingsAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("There is no savings account with id: %d", id)));
    }

    @Scheduled(cron = "${scheduler.savings-account.rate-calculation.cron}")
    @Transactional
    protected void calculateSavingsAccountRatePercent() {
        List<SavingsAccount> accounts = savingsAccountRepository.findAll();

        for (int i = 0; i <= accounts.size(); i += batchSize) {
            int endIndex = Math.min(accounts.size(), i + batchSize);

            List<SavingsAccount> subList = accounts.subList(i, endIndex);
            threadPoolTaskExecutor.execute(() -> {
                subList.forEach(this::calculateRatePercent);
                savingsAccountRepository.saveAll(subList);
            });
        }
    }

    private Tariff getCurrentSavingsAccountTariff(SavingsAccount savingsAccount) {
        int currentTariffIndex = savingsAccount.getTariffHistory().size() - 1;
        return savingsAccount.getTariffHistory().get(currentTariffIndex).getTariff();
    }

    private float getCurrentTariffRatePercent(Tariff tariff) {
        int currentRateIndex = tariff.getRateHistory().size() - 1;
        return tariff.getRateHistory().get(currentRateIndex).getPercent();
    }

    private void calculateRatePercent(SavingsAccount account) {
        BigDecimal oldBalance = account.getBalance();
        BigDecimal percent = BigDecimal.valueOf(getCurrentTariffRatePercent(getCurrentSavingsAccountTariff(account)));
        BigDecimal newBalance = oldBalance
                .divide(BigDecimal.valueOf(100))
                .multiply(percent)
                .add(oldBalance);

        account.setBalance(newBalance);
    }

    private void validateRequestMoneyAmount(BigDecimal moneyAmount){
        if (moneyAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new DataValidationException("The amount you are trying to deposit into the savings account must be greater than zero");
        }
    }

    private void checkSavingsAccountAlreadyExist(Account account) {
        if (account.getSavingsAccount() != null) {
            throw new AccountAlreadyExistException(String.format("Account with id: %d, already has a savings account", account.getId()));
        }
    }

    private void checkSavingsAccountDoesNotExist(Account account) {
        if (account.getSavingsAccount() == null) {
            throw new NotFoundException(String.format("There no savings account in account with id: %d", account.getId()));
        }
    }

    private void validateAccountTypeAndStatus(Account account) {
        AccountType accountType = account.getAccountType();
        AccountStatus accountStatus = account.getStatus();

        if (accountType != AccountType.SAVINGS) {
            throw new InvalidTypeException(String.format("Your account type: %s must be type of %s", accountType, AccountType.SAVINGS));
        } else if (accountStatus != AccountStatus.OPEN) {
            throw new InvalidStatusException("Account status must be open");
        }
    }
}
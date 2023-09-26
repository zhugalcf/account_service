package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountCreateDto;
import faang.school.accountservice.dto.SavingsAccountResponseDto;
import faang.school.accountservice.dto.SavingsAccountUpdateDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.OwnerType;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.exception.AccountAlreadyExistException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountService accountService;
    private final TariffService tariffService;
    private final SavingsAccountMapper savingsAccountMapper;
    private final SavingsAccountResponseMapper savingsAccountResponseMapper;

    @Transactional
    public SavingsAccountResponseDto openSavingsAccount(SavingsAccountCreateDto savingsAccountCreateDto) {
        long accountId = savingsAccountCreateDto.getAccountId();
        TariffType tariffType = savingsAccountCreateDto.getTariffType();

        Account account = accountService.getAccountById(accountId);

        checkSavingsAccountAlreadyExist(account);
        validateAccountTypeAndStatus(account);
        log.info("Account with ID: {}, successfully passed all the necessary validations.", accountId);

        SavingsAccount accountToSave = savingsAccountMapper.toEntity(savingsAccountCreateDto);
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
        TariffType newTariffType = updateDto.getTariffType();

        SavingsAccount savingsAccount = findSavingsAccountBy(savingsAccountId);
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

    public SavingsAccountResponseDto getSavingsAccountBy(long id) {
        return savingsAccountResponseMapper.toDto(findSavingsAccountBy(id));
    }

    public SavingsAccountResponseDto getSavingsAccountByOwnerIdAndOwnerType(long ownerId, OwnerType ownerType) {
        Account account = accountService.findAccountByOwnerIdAndOwnerType(ownerId, ownerType);
        checkSavingsAccountDoesNotExist(account);
        return savingsAccountResponseMapper.toDto(account.getSavingsAccount());
    }

    public SavingsAccount findSavingsAccountBy(long id) {
        return savingsAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("There is no savings account with id: %d", id)));
    }

    private Tariff getCurrentSavingsAccountTariff(SavingsAccount savingsAccount) {
        int currentTariffIndex = savingsAccount.getTariffHistory().size() - 1;
        return savingsAccount.getTariffHistory().get(currentTariffIndex).getTariff();
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
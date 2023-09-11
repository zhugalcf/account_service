package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.enums.TariffType;
import faang.school.accountservice.exception.InvalidStatusException;
import faang.school.accountservice.exception.InvalidTypeException;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.mapper.SavingsAccountResponseMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.TariffHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountService accountService;
    private final SavingsAccountMapper savingsAccountMapper;
    private final TariffHistoryRepository tariffHistoryRepository;
    private final TariffService tariffService;
    private final SavingsAccountResponseMapper savingsAccountResponseMapper;

    @Transactional
    public SavingsAccountDto openSavingsAccount(SavingsAccountDto savingsAccountDto) {
        long accountId = savingsAccountDto.getAccountId();
        Account account = accountService.getAccountById(accountId);

        AccountType accountType = account.getAccountType();
        AccountStatus accountStatus = account.getStatus();

        validateAccountType(accountType);
        validateAccountStatus(accountStatus);

        SavingsAccount accountToSave = savingsAccountMapper.toEntity(savingsAccountDto);
        accountToSave.setAccount(account);
        accountToSave.setVersion(1);

        Tariff tariff = tariffService.getTariff(savingsAccountDto.getTariffType());
        TariffHistory tariffHistory = TariffHistory.builder()
                .savingsAccount(accountToSave)
                .tariff(tariff)
                .build();
        tariffHistoryRepository.save(tariffHistory);
//        accountToSave.setTariffHistory(new ArrayList<>(List.of(tariffHistory)));


        savingsAccountRepository.save(accountToSave);
        return savingsAccountMapper.toDto(accountToSave);
    }



    //    public SavingsAccountResponseDto getSavingsAccountByAccountId(long id) {
//        SavingsAccount account = savingsAccountRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(String.format("There is no account with id: %d", id)));
//        return savingsAccountResponseMapper.toDto(account);
//    }
//
//    public SavingsAccountResponseDto getSavingsAccountByOwnerId(long ownerId) {
//        Account account = accountService.getAccountByOwnerId(ownerId);
//        validateSavingsAccountExists(account);
//        return savingsAccountResponseMapper.toDto(account.getSavingsAccount());
//    }
//    public SavingsAccount getSavingsAccountById(long accountId) {
//
//    }

    private void validateSavingsAccountExists(Account account) {
        if (account.getSavingsAccount() == null) {
            throw new NotFoundException(String.format("There no savings account in account with id: %d", account.getId()));
        }
    }

    private void validateAccountType(AccountType accountType) {
        if (accountType != AccountType.SAVINGS) {
            throw new InvalidTypeException(String.format("Your account type: %s must be type of %s", accountType, AccountType.SAVINGS));
        }
    }

    private void validateAccountStatus(AccountStatus accountStatus) {
        if (accountStatus != AccountStatus.OPEN) {
            throw new InvalidStatusException("Account status must be open");
        }
    }
}

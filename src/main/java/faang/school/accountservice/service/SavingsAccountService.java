package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.exception.InvalidStatusException;
import faang.school.accountservice.exception.InvalidTypeException;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountRepository accountRepository;
    private final SavingsAccountMapper savingsAccountMapper;

    @Transactional
    public SavingsAccountDto openSavingsAccount(SavingsAccountDto savingsAccountDto) {
        long accountId = savingsAccountDto.getAccountId();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(String.format("There is no account with id: %d", accountId)));

        AccountType accountType = account.getAccountType();
        AccountStatus accountStatus = account.getStatus();

        validateAccountType(accountType);
        validateAccountStatus(accountStatus);

        SavingsAccount accountToSave = savingsAccountMapper.toEntity(savingsAccountDto);
        accountToSave.setAccount(account);
        savingsAccountRepository.save(accountToSave);
        return savingsAccountMapper.toDto(accountToSave);
    }

    public void getSavingsAccountBy(long id){

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

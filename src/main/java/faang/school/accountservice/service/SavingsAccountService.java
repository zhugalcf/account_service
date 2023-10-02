//package faang.school.accountservice.service;
//
//import faang.school.accountservice.entity.Owner;
//import faang.school.accountservice.entity.account.AccountStatus;
//import faang.school.accountservice.entity.account.AccountType;
//import faang.school.accountservice.entity.account.Currency;
//import faang.school.accountservice.entity.account.SavingsAccount;
//import faang.school.accountservice.mapper.SavingsAccountMapper;
//import faang.school.accountservice.repository.SavingsAccountRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.OptimisticLockingFailureException;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import faang.school.accountservice.dto.SavingsAccountDto;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class SavingsAccountService {
//    private final OwnerService ownerService;
//    private final CurrencyService currencyService;
//    private final SavingsAccountRepository savingsAccountRepository;
//    private final SavingsAccountMapper savingsAccountMapper;
//    private final UniqueNumberService uniqueNumberService;
//
//    @Transactional
//    @Retryable(retryFor = {OptimisticLockingFailureException.class})
//    public SavingsAccountDto openSavingsAccount(Long ownerId, String currencyCode) {
//        Owner owner = ownerService.getOwner(ownerId);
//        Currency currency = currencyService.getCurrency(currencyCode);
//        String accountNumber = uniqueNumberService.getFreeAccountNumber(AccountType.SAVINGS_ACCOUNT);
//
//        SavingsAccount savingsAccount = new SavingsAccount();
//        savingsAccount.setAccountStatus(AccountStatus.OPENED);
//
//        savingsAccount.getAccount().setOwner(owner);
//        savingsAccount.getAccount().setCurrency(currency);
//        savingsAccount.getAccount().setAccountNumber(accountNumber);
//
//        SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);
//
//        SavingsAccountDto savingsAccountDto = new SavingsAccountDto();
//        savingsAccountDto.setId(savedAccount.getId());
//
//        return savingsAccountDto;
//    }
//
//    @Transactional(readOnly = true)
//    public SavingsAccountDto getSavingsAccountById(Long id) {
//        SavingsAccount account = savingsAccountRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("SavingsAccount with id " + id + " not found"));
//
//        return savingsAccountMapper.toSavingsDto(account);
//    }
//
//    @Transactional(readOnly = true)
//    public SavingsAccountDto getSavingsAccountInfoById(Long id) {
//        SavingsAccount account = savingsAccountRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("SavingsAccount with id " + id + " not found"));
//
//        return savingsAccountMapper.toSavingsDtoWithTariffAndRate(account);
//    }
//}
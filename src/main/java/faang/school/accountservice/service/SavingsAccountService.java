package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountTariffHistoryDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.entity.SavingsAccountTariffHistory;
import faang.school.accountservice.entity.Tariff;
import faang.school.accountservice.entity.TariffType;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.account.Currency;
import faang.school.accountservice.entity.account.SavingsAccount;
import faang.school.accountservice.exception.TariffNotFoundException;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.SavingsAccountTariffHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import faang.school.accountservice.dto.SavingsAccountDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsAccountService {
    private final OwnerService ownerService;
    private final CurrencyService currencyService;
    private final SavingsAccountRepository savingsAccountRepository;
    private final SavingsAccountTariffHistoryRepository savingsAccountTariffHistoryRepository;
    private final SavingsAccountMapper savingsAccountMapper;
    private final UniqueNumberService uniqueNumberService;
    private final TariffService tariffService;


    @Transactional
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public SavingsAccountDto openSavingsAccount(Long ownerId, String currencyCode) {
        Owner owner = ownerService.getOwner(ownerId);
        Currency currency = currencyService.getCurrency(currencyCode);
        String accountNumber = uniqueNumberService.getFreeAccountNumber(AccountType.SAVINGS_ACCOUNT);

        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountStatus(AccountStatus.OPENED);

        savingsAccount.getAccount().setOwner(owner);
        savingsAccount.getAccount().setCurrency(currency);
        savingsAccount.getAccount().setAccountNumber(accountNumber);

        SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);

        SavingsAccountDto savingsAccountDto = new SavingsAccountDto();
        savingsAccountDto.setId(savedAccount.getId());

        return savingsAccountDto;
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


    @Async
    @jakarta.transaction.Transactional
    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    public void calculateAndApplyLastPercent() {

        List<SavingsAccount> savingsAccounts = savingsAccountRepository.findAllByAccountStatus(AccountStatus.OPENED);

        for (SavingsAccount savingsAccount : savingsAccounts) {
            // Ваш код для расчета процентов и обновления баланса
            // Можно использовать дату последнего успешного начисления процентов для определения периода расчета
            // ...

            // Обновление даты последнего успешного начисления процентов
            savingsAccount.setLastUpdateCalculationAt(LocalDateTime.now());
        }
    }

}
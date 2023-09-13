package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.repository.SavingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Transactional
public class SavingsAccountService {
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final SavingAccountRepository savingAccountRepository;
    private final TariffService tariffService;
    private final Executor executor;


    public SavingsAccountDto openAccount(SavingsAccountDto accountDTO) {
        var savingAccount = accountMapper.toEntity(accountDTO);
        var account = accountService.getAccountById(accountDTO.getAccountId());
        var tariff = tariffService.getTariffById(accountDTO.getCurrent_tariff());
        savingAccount.setAccount(account);
        savingAccount.setCurrent_tariff(tariff);
        var savedAccount = savingAccountRepository.save(savingAccount);
        return accountMapper.toDto(savedAccount);
    }

    public SavingsAccountDto getSavingAccount(long accountId) {
        var account = savingAccountRepository.findById(accountId).orElseThrow(
                () -> new RuntimeException("Account with id: " + accountId + " wasn`t found")
        );
        var tariffDto = accountMapper.toDto(account.getCurrent_tariff());
        var accountDto = accountMapper.toDto(account);
        accountDto.setTariffDto(tariffDto);
        return accountDto;
    }

    @Transactional
    public void updateInterest(int batchSize) {
        var accounts = savingAccountRepository.findAll();
        for (int i = 0; i < accounts.size(); i++) {
            var accountsBatch = accounts.subList(i, Math.min(accounts.size(), i + batchSize));
            executor.execute(() -> {
                accountsBatch.forEach(account -> {
                    BigDecimal currentRate = BigDecimal.valueOf(account.getCurrent_tariff().getCurrentRate());
                    BigDecimal newBalance = account.getBalance().add(account.getBalance().multiply(currentRate));
                    account.setBalance(newBalance);
                });
            });
        }
    }
}
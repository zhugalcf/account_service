package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.dto.TariffDto;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.repository.SavingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavingsAccountService {
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final SavingAccountRepository savingAccountRepository;
    private final TariffService tariffService;

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
}
package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.UpdateBalanceDto;
import faang.school.accountservice.entity.Account;
import faang.school.accountservice.entity.Balance;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    @Transactional
    public AccountDto createAccount(Long accountNumber) {
        Account account = Account.builder().accountNumber(accountNumber).build();
        Account savedAccount = accountRepository.save(account);
        log.info(account.getId() + " is saved to db");

        saveBalance(savedAccount);

        return accountMapper.toDto(savedAccount);
    }

    @Transactional(readOnly = true)
    public BalanceDto getBalance(Long balanceId) {
        Balance balance = balanceExistsValidation(balanceId);
        log.info(balance + " is retrieved");
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto updateBalance(UpdateBalanceDto updateBalanceDto, Long balanceId) {
        validateOwnership(updateBalanceDto, balanceId);
        Balance balance = balanceExistsValidation(updateBalanceDto.getId());

        BigDecimal currentBalance = balance.getCurrentBalance();
        BigDecimal deposit = updateBalanceDto.getDeposit();

        BigDecimal updatedBalance = currentBalance.add(deposit);

        balance.setBalanceVersion(balance.getBalanceVersion() + 1);
        balance.setCurrentBalance(updatedBalance);

        Balance savedBalance = balanceRepository.save(balance);
        log.info(balance + " is updated");

        return balanceMapper.toDto(savedBalance);
    }

    private void validateOwnership(UpdateBalanceDto updateBalanceDto, Long balanceId) {
        if (!Objects.equals(balanceId, updateBalanceDto.getId())) {
            throw new IllegalArgumentException("You can only update your own balance");
        }
    }

    private void saveBalance(Account savedAccount) {
        Balance balance = Balance.builder()
                .account(savedAccount)
                .authorizationBalance(BigDecimal.ZERO)
                .currentBalance(BigDecimal.ZERO)
                .balanceVersion(0L)
                .build();
        balanceRepository.save(balance);
        log.info(balance + " is saved to db");
    }

    private Balance balanceExistsValidation(Long balanceId) {
        return balanceRepository.findById(balanceId)
                .orElseThrow(() -> new EntityNotFoundException("Balance with id: " + balanceId + " not found"));
    }
}

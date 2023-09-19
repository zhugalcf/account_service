package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.exception.IncorrectAccountBalanceLengthException;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;
    private final BalanceMapper balanceMapper;

    @Transactional
    public BalanceDto getBalance(long accountId) {
        Account account = getAccountNumber(accountId);
        String accountNumber = account.getNumber();

        Balance balance = balanceRepository.findBalanceByAccountNumber(accountNumber);
        log.info("Balance with number = {} has taken from DB successfully", accountNumber);
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto create(long accountId) {
        Account account = getAccountNumber(accountId);
        String accountNumber = account.getNumber();

        Balance balance = Balance.builder()
                .account(account)
                .currentAuthorizationBalance(new BigDecimal(0.0))
                .currentActualBalance(new BigDecimal(0.0))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1L)
                .build();

        Balance savedBalance = balanceRepository.save(balance);
        log.info("New balance with account number ={} was created successfully", account.getNumber());
        return balanceMapper.toDto(savedBalance);
    }

    @Transactional
    public BalanceDto update(BalanceDto balanceDto) {
        Balance balance = balanceRepository.findBalanceByAccountNumber(balanceDto.getAccountNumber());

        balance.setCurrentAuthorizationBalance(balanceDto.getCurrentAuthorizationBalance());
        balance.setCurrentActualBalance(balanceDto.getCurrentActualBalance());
        balance.setUpdatedAt(LocalDateTime.now());
        return balanceMapper.toDto(balance);
    }

    private Account getAccountNumber(long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new EntityNotFoundException("Entity account not found"));
    }
}

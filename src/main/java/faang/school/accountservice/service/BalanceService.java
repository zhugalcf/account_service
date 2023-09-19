package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.balance.Balance;
import faang.school.accountservice.excpetion.EntityNotFoundException;
import faang.school.accountservice.excpetion.InsufficientBalanceException;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;
    private final AccountService accountService;

    @Transactional(readOnly = true)
    public BalanceDto getBalanceByAccountId(Long accountId) {
        Balance balance = getBalance(accountId);
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto createBalance(Long accountId, BalanceDto balanceDto) {
        Account account = accountService.getAccount(accountId);
        Balance balance = Balance.builder()
                .authorizationBalance(balanceDto.getAuthorizationBalance())
                .actualBalance(balanceDto.getActualBalance())
                .account(account)
                .build();

        return balanceMapper.toDto(balanceRepository.save(balance));
    }

    @Transactional
    public BalanceDto updateBalance(Long accountId, BalanceDto balanceDto) {
        Balance balance = getBalance(accountId);
        balanceMapper.updateBalanceFromBalanceDto(balanceDto, balance);

        return balanceMapper.toDto(balanceRepository.save(balance));
    }

    @Transactional
    public BalanceDto deposit(Long accountId, BigDecimal amount) {
        Balance balance = getBalance(accountId);
        BigDecimal currentAuthorizationBalance = balance.getAuthorizationBalance();

        balance.setAuthorizationBalance(currentAuthorizationBalance.add(amount));
        balance.setActualBalance(balance.getActualBalance().add(amount));

        Balance saved = balanceRepository.save(balance);
        return balanceMapper.toDto(saved);
    }

    @Transactional
    public BalanceDto withdraw(Long accountId, BigDecimal amount) {
        Balance balance = getBalance(accountId);
        BigDecimal currentAuthorizationBalance = balance.getAuthorizationBalance();

        if (currentAuthorizationBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Not enough authorization balance.");
        }

        balance.setAuthorizationBalance(currentAuthorizationBalance.subtract(amount));
        balance.setActualBalance(balance.getActualBalance().subtract(amount));

        Balance saved = balanceRepository.save(balance);
        return balanceMapper.toDto(saved);
    }

    @Transactional
    public void transfer(Long senderId, Long receiverId, BigDecimal amount) {
        Balance senderBalance = getBalance(senderId);
        Balance receiverBalance = getBalance(receiverId);

        BigDecimal senderAuthorizationBalance = senderBalance.getAuthorizationBalance();

        if (senderAuthorizationBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Not enough authorization balance to transfer.");
        }

        senderBalance.setAuthorizationBalance(senderAuthorizationBalance.subtract(amount));
        senderBalance.setActualBalance(senderBalance.getActualBalance().subtract(amount));

        receiverBalance.setActualBalance(receiverBalance.getActualBalance().add(amount));

        balanceRepository.save(senderBalance);
        balanceRepository.save(receiverBalance);
    }

    private Balance getBalance(Long accountId) {
        return balanceRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + accountId + " not found"));
    }
}

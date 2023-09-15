package faang.school.accountservice.service;

import faang.school.accountservice.dto.balance.BalanceDto;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;

    @Transactional
    public BalanceDto create(Long accountId) {
        Balance balance = createBalance(accountId);

        balance = balanceRepository.save(balance);

        log.info("Created balance: {}", balance);
        return balanceMapper.toDto(balance);
    }

    private Balance createBalance(Long accountId) {
        Account account = accountRepository.getReferenceById(accountId);
        return Balance.builder()
                .account(account)
                .authBalance(BigDecimal.ZERO)
                .actualBalance(BigDecimal.ZERO)
                .build();
    }
}

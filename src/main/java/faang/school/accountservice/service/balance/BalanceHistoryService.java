package faang.school.accountservice.service.balance;

import faang.school.accountservice.dto.balance.BalanceUpdateType;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.BalanceHistory;
import faang.school.accountservice.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceHistoryService {

    private final BalanceHistoryRepository balanceHistoryRepository;

    public void save(Balance balance, BalanceUpdateType type, BigDecimal amount) {
        BalanceHistory balanceHistory = createBalanceHistory(balance, type, amount);

        balanceHistoryRepository.save(balanceHistory);
        log.info("Saved balance history: {}", balanceHistory);
    }

    private BalanceHistory createBalanceHistory(Balance balance, BalanceUpdateType type, BigDecimal amount) {
        return BalanceHistory.builder()
                .balance(balance)
                .type(type)
                .amount(amount)
                .build();
    }
}

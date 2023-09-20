package faang.school.accountservice.service.balance;

import faang.school.accountservice.dto.balance.BalanceUpdateDto;
import faang.school.accountservice.dto.balance.BalanceUpdateType;
import faang.school.accountservice.model.Balance;
import org.springframework.stereotype.Component;

@Component
public class BalanceClearing implements BalanceUpdater {
    @Override
    public boolean isApplicable(BalanceUpdateDto dto) {
        return dto.type() == BalanceUpdateType.CLEARING;
    }

    @Override
    public Balance update(Balance balance, BalanceUpdateDto dto) {
        balance.setActualBalance(balance.getActualBalance().subtract(dto.amount()));
        return balance;
    }
}

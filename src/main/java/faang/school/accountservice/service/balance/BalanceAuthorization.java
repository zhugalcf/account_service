package faang.school.accountservice.service.balance;

import faang.school.accountservice.dto.balance.BalanceUpdateDto;
import faang.school.accountservice.dto.balance.BalanceUpdateType;
import faang.school.accountservice.model.Balance;
import org.springframework.stereotype.Component;

@Component
public class BalanceAuthorization implements BalanceUpdater {
    @Override
    public boolean isApplicable(BalanceUpdateDto dto) {
        return dto.type() == BalanceUpdateType.AUTHORIZATION;
    }

    @Override
    public Balance update(Balance balance, BalanceUpdateDto dto) {
        balance.setAuthBalance(
                balance.getAuthBalance()
                        .subtract(dto.amount()));
        return balance;
    }
}

package faang.school.accountservice.service.balance;

import faang.school.accountservice.dto.balance.BalanceUpdateDto;
import faang.school.accountservice.model.Balance;

public interface BalanceUpdater {

    boolean isApplicable(BalanceUpdateDto dto);

    Balance update(Balance balance, BalanceUpdateDto dto);
}

package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.exception.IncorrectAccountBalanceLengthException;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;

    public BalanceDto getBalance(String accountNumber) {
        validateAccountNumber(accountNumber);
        Balance balance = balanceRepository.findBalanceByAccountNumber(accountNumber);
        log.info("Balance with number = {} has taken from DB successfully", accountNumber);
        return balanceMapper.toDto(balance);
    }

    public void create() {

    }

    public void update() {

    }

    private void validateAccountNumber(String accountNumber) {
        if (accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account Number is empty");
        }
        if (accountNumber.length() < 12 || accountNumber.length() > 20) {
            throw new IncorrectAccountBalanceLengthException("The length of account number must be more then 12 and less then 20");
        }
    }
}

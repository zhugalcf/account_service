package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.exception.IncorrectAccountBalanceLengthException;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Transactional
    public void create(BalanceDto balanceDto) {
        Balance balance = balanceMapper.toModel(balanceDto);
        balance.setVersion(1L);
        balance.setCreatedAt(LocalDateTime.now());
        balance.setUpdatedAt(LocalDateTime.now());

        balanceRepository.save(balance);
        log.info("New balance with account number ={} was created successfully", balance.getAccountNumber());
    }

    @Transactional
    public void update(BalanceDto balanceDto) {
        Balance balance = balanceRepository.findBalanceByAccountNumber(balanceDto.getAccountNumber());
        balance.setCurrentAuthorizationBalance(balanceDto.getCurrentAuthorizationBalance());
        balance.setCurrentActualBalance(balanceDto.getCurrentActualBalance());
        balance.setUpdatedAt(LocalDateTime.now());
        balance.incrementVersion();
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

package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.balance.Balance;
import faang.school.accountservice.excpetion.EntityNotFoundException;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private Balance getBalance(Long accountId) {
        return balanceRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + accountId + " not found"));
    }
}

package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountCreationRequest;
import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.UpdateBalanceDto;
import faang.school.accountservice.entity.Balance;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.entity.owner.Owner;
import faang.school.accountservice.enums.Currency;
import faang.school.accountservice.exception.BalanceNotFoundException;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceRepository;
import faang.school.accountservice.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final OwnerRepository ownerRepository;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;
    private final FreeAccountNumberService freeAccountNumberService;

    @Transactional
    public AccountDto createAccount(AccountCreationRequest accountCreationRequest){
        Owner owner = ownerRepository.getById(accountCreationRequest.getOwnerId());
        BigInteger accountNumber = (freeAccountNumberService.getFreeNumber(accountCreationRequest.getType(),
                        number -> System.out.println("The generated number is " +
                                number)));
        Account account = Account.builder()
                .number(accountNumber)
                .owner(owner)
                .type(accountCreationRequest.getType())
                .currency(accountCreationRequest.getCurrency())
                .status(accountCreationRequest.getStatus())
                .createdTime(LocalDateTime.now())
                .build();
        Account savedAccount = accountRepository.save(account);
        log.info("Account {} is saved to db", account.getId());

        Balance balance = Balance.builder()
                .account(savedAccount)
                .authorizationBalance(new BigDecimal(0))
                .currentBalance(new BigDecimal(0))
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .balanceVersion(0L)
                .build();
        balanceRepository.save(balance);
        log.info(balance.toString() + " is saved to db");

        return accountMapper.toDto(savedAccount);
    }

    public BalanceDto getBalance(Long id){
        Balance balance = balanceRepository.findById(id)
                .orElseThrow(() -> new BalanceNotFoundException(id));
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto updateBalance(UpdateBalanceDto updateBalanceDto){
        Balance balance = balanceRepository.findById(updateBalanceDto.getBalanceId())
                .orElseThrow(() -> new BalanceNotFoundException(updateBalanceDto.getBalanceId()));

        BigDecimal currentBalance = balance.getCurrentBalance();
        BigDecimal deposit = updateBalanceDto.getDeposit();

        BigDecimal sum = currentBalance.add(deposit);

        balance.setCurrentBalance(sum);
        balance.setUpdated(LocalDateTime.now());
        balanceRepository.save(balance);

        log.info(balance + " is updated");

        return balanceMapper.toDto(balance);
    }
}

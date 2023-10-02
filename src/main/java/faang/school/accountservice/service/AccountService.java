package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.dto.ReplenishmentRequest;
import faang.school.accountservice.entity.Account;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.exception.DataValidationException;
import faang.school.accountservice.exception.GenerateAccountNumberException;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.repository.AccountRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDto getAccountById(Long id) {
        return accountMapper.toDto(findById(id));
    }

    @Transactional
    public AccountDto open(AccountDto accountDto) {
        Account account = accountMapper.toEntity(accountDto);
        account.setNumber(generateAccountNumber(accountDto.getOwnerId(), System.currentTimeMillis()));
        try {
            return save(account);
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate account number. Regenerating...");
            account.setNumber(generateAccountNumber(accountDto.getOwnerId(), System.currentTimeMillis()));
            return save(account);
        }
    }


    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 5)
    public void block(Long id) {
        Account account = findById(id);
        accountBlockedCheck(account);
        account.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(account);
        log.info("Account by ID - " + id + " has been BLOCKED");
    }

    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 5)
    public void close(Long id) {
        Account account = findById(id);
        accountClosedCheck(account);
        account.setStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        accountRepository.save(account);
        log.info("Account by ID - " + id + " has been CLOSED");
    }

    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 5)
    public void deposit(Long id, ReplenishmentRequest request) {
        if (request.sum().compareTo(BigDecimal.ZERO) <= 0)
            throw new DataValidationException("The deposit amount must be greater than zero");

        Account account = findById(id);
        accountBlockedCheck(account);
        accountClosedCheck(account);
        account.setBalance(account.getBalance().add(request.sum()));
        save(account);
    }

    private void accountBlockedCheck(Account account) {
        if (account.getStatus() == AccountStatus.BLOCKED)
            throw new DataValidationException("Account is blocked");
    }

    private void accountClosedCheck(Account account) {
        if (account.getStatus() == AccountStatus.CLOSED)
            throw new DataValidationException("Account is closed");
    }

    private Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new DataValidationException("Account by ID - " + id + " not found"));
    }

    private AccountDto save(Account account) {
        return accountMapper.toDto(accountRepository.save(account));
    }

    private String generateAccountNumber(long userId, long timestamp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String inputData = Long.toString(userId) + Long.toString(timestamp);
            md.update(inputData.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            String hexString = String.format("%064x", new BigInteger(1, digest));
            return hexString.substring(0, 20);
        } catch (NoSuchAlgorithmException e) {
            throw new GenerateAccountNumberException(e.getMessage());
        }

    }
}

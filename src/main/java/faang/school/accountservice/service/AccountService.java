package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.entity.Account;
import faang.school.accountservice.enums.Status;
import faang.school.accountservice.exception.EntityNotFoundException;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountDto open(AccountDto accountDto) {
        accountDto.setStatus(Status.ACTIVE);
        Account account = accountRepository.save(accountMapper.toEntity(accountDto));
        return accountMapper.toDto(account);
    }

    public AccountDto get(long id) {
        return accountMapper.toDto(getAccount(id));
    }

    @Transactional
    public AccountDto block(long id, Status status) {
        Account account = getAccount(id);
        account.setStatus(status);
        return accountMapper.toDto(account);
    }

    @Transactional
    public AccountDto close(long id) {
        Account account = getAccount(id);
        account.setStatus(Status.CLOSED);
        account.setClosingDate(LocalDateTime.now());
        return accountMapper.toDto(account);
    }

    private Account getAccount(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }
}

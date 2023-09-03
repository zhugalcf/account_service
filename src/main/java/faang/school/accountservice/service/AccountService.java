package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.entity.Owner;
import faang.school.accountservice.entity.account.Account;
import faang.school.accountservice.entity.account.AccountStatus;
import faang.school.accountservice.excpetion.EntityNotFoundException;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final OwnerService ownerService;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public AccountDto get(Long id) {
        Account account = getAccount(id);
        return accountMapper.toDto(account);
    }

    @Transactional
    public AccountDto open(AccountDto accountDto) {
        Account account = accountMapper.toEntity(accountDto);

        Owner owner = ownerService.getOwner(accountDto.getOwnerId());
        account.setOwner(owner);
        account.setAccountStatus(AccountStatus.OPENED);

        return accountMapper.toDto(accountRepository.save(account));
    }

    @Transactional
    public AccountDto block(Long id) {
        Account account = getAccount(id);
        account.setAccountStatus(AccountStatus.BLOCKED);
        return accountMapper.toDto(account);
    }

    @Transactional
    public AccountDto close(Long id) {
        Account account = getAccount(id);
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        return accountMapper.toDto(account);
    }


    public Account getAccount(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + id + " not found"));
    }
}

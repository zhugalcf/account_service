package faang.school.accountservice.service;

import faang.school.accountservice.dto.AccountDto;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private AccountRepository accountRepository;

    public AccountDto getAccount(Long accountId) {
        // Реализация получения аккаунта
    }

    public AccountDto openAccount(AccountRequestDto accountRequest) {
        // Реализация создания аккаунта
    }

    public void blockAccount(Long accountId) {
        // Реализация блокировки аккаунта
    }

    public void closeAccount(Long accountId) {
        // Реализация закрытия аккаунта
    }
}

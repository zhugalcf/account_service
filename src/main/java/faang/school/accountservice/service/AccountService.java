package faang.school.accountservice.service;

import faang.school.accountservice.model.Account;
import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account getAccountById(long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No account with id: " + id)
        );
    }
}

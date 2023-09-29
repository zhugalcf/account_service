package faang.school.accountservice.service;

import faang.school.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public void open(){

    }

    @Transactional
    public void close(){

    }

    @Transactional
    public void block(){

    }

    @Transactional(readOnly = true)
    public void get(){

    }
}

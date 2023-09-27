package faang.school.accountservice.service;

import faang.school.accountservice.dto.account.CreateAccountDto;
import faang.school.accountservice.dto.account.FreeAccountNumberDto;
import faang.school.accountservice.enums.AccountType;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.mapper.FreeAccNumMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.FreeAccountNumber;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.FreeAccountNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeneratorNumbersService {
    private FreeAccountNumberRepository freeAccountNumberRepository;
    private AccountRepository accountRepository;
    private FreeAccNumMapper freeAccNumMapper;

    @Transactional
    public void generateNumberOfType(long n, AccountType accountType) {
        Optional<FreeAccountNumber> freeAccountNumber = freeAccountNumberRepository.findByAccountType(accountType);
        FreeAccountNumberDto freeAccountNumberDto = freeAccNumMapper.toDto(freeAccountNumber);

        for (int i = 0; i < n; i++) {
            UUID uuid = UUID.randomUUID();
            freeAccountNumberDto.setAccountNumber(String.valueOf(uuid));
        }
        freeAccountNumberRepository.save(freeAccNumMapper.toEntity(freeAccountNumberDto));
    }
}

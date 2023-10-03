package faang.school.accountservice.service;

import faang.school.accountservice.dto.SavingsAccountDto;
import faang.school.accountservice.entity.SavingsAccount;
import faang.school.accountservice.entity.Tariff;
import faang.school.accountservice.exception.EntityNotFoundException;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.service.util.JsonMapper;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final TariffService tariffService;
    private final SavingsAccountMapper savingsAccountMapper;
    private final JsonMapper jsonMapper;

    @Transactional
    public SavingsAccountDto open(SavingsAccountDto savingsAccountDto) {
        SavingsAccount savingsAccount = savingsAccountMapper.toEntity(savingsAccountDto);

        List<Long> tariffId = List.of(savingsAccountDto.getTariffId());
        String historyTariff = jsonMapper.toJsonArray(tariffId);

        savingsAccount.setHistoryTariff(historyTariff);
        savingsAccount = savingsAccountRepository.save(savingsAccount);
        return savingsAccountMapper.toDto(savingsAccount);
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public SavingsAccountDto updateTariff(long id, long tariffId) {
        SavingsAccount savingsAccount = getSavingsAccount(id);

        tariffService.getTariff(tariffId);

        String updateTariffHistory = jsonMapper.addToJsonArray(savingsAccount.getHistoryTariff(), tariffId);
        savingsAccount.setHistoryTariff(updateTariffHistory);
        savingsAccount.setTariff(Tariff.builder()
                .id(tariffId)
                .build());

        savingsAccount = savingsAccountRepository.save(savingsAccount);
        return savingsAccountMapper.toDto(savingsAccount);
    }

    @Transactional(readOnly = true)
    public SavingsAccountDto getSavingAccountById(long id) {
        return savingsAccountMapper.toDto(getSavingsAccount(id));
    }

    @Transactional(readOnly = true)
    public SavingsAccountDto getSavingAccountByAccountId(long accountId) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return savingsAccountMapper.toDto(savingsAccount);
    }

    private SavingsAccount getSavingsAccount(long id) {
        return savingsAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }
}

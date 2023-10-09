package faang.school.accountservice.service;

import faang.school.accountservice.config.account.AccountConfig;
import faang.school.accountservice.enums.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledAccountGenerationService {
    private final FreeAccountNumbersService freeAccountNumbersService;
    private final AccountConfig accountConfig;
    private AccountType accountType;

    @Scheduled(fixedDelay = 180000)
    public void generateAccountsScheduled() {
        generateAccounts();
    }

    public void generateAccounts() {
        int amountToGenerate = accountConfig.getAmountToGenerate();
        int retryDelay = accountConfig.getRetryDelay();

        freeAccountNumbersService.generateAccountNumber(amountToGenerate, accountType);
    }
}

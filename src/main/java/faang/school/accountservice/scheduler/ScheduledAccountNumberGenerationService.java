package faang.school.accountservice.scheduler;

import faang.school.accountservice.config.account.AccountGenerationConfig;
import faang.school.accountservice.entity.account.AccountType;
import faang.school.accountservice.service.FreeAccountNumbersService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledAccountNumberGenerationService {
    private final FreeAccountNumbersService freeAccountNumbersService;
    private final AccountGenerationConfig accountGenerationConfig;

    @Scheduled(cron = "${spring.account.generation.schedule.cron}")
    public void generateAccountNumbers() {
        long numberOfAccounts = accountGenerationConfig.getNumberOfAccounts();
        AccountType[] accountTypes = AccountType.values();

        for (AccountType accountType : accountTypes) {
            freeAccountNumbersService.generateAccountNumbersToReach(numberOfAccounts, accountType);
        }
    }
}

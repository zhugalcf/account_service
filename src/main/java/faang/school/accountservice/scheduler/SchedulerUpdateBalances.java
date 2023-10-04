package faang.school.accountservice.scheduler;

import faang.school.accountservice.service.SavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerCalculationPercent {
    private final SavingsAccountService savingsAccountService;

    @Scheduled(cron = "${spring.scheduler.cron}")
    public void calculateInterest() {
        savingsAccountService.calculateAndApplyLastPercent();
    }
}
//package faang.school.accountservice.scheduler;
//
//import faang.school.accountservice.service.PercentCalculationService;
//import faang.school.accountservice.service.SavingsAccountService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class SchedulerCalculationPercent {
//    private final PercentCalculationService percentCalculationService;
//
//    @Scheduled(cron = "${spring.scheduler.calculationPercent.schedule.cron}")
//    public void calculateLastPercent() {
//        percentCalculationService.calculateAndApplyLastPercent();
//    }
//}
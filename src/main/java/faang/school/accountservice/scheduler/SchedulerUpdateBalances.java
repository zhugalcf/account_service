//package faang.school.accountservice.scheduler;
//
//import faang.school.accountservice.service.PercentCalculationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class SchedulerUpdateBalances {
//    private final PercentCalculationService percentCalculationService;
//
//    @Scheduled(cron = "${spring.scheduler.cron}")
//    public void percentCalculateAndApply() {
//        percentCalculationService.percentCalculateAndApply();
//    }
//}
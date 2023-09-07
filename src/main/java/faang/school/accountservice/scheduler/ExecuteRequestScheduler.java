package faang.school.accountservice.scheduler;

import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecuteRequestScheduler {
    private final RequestService requestService;

    @Async("taskExecutor")
    @Scheduled(cron = "${scheduler.execute-request.cron}")
    public void executeRequests(){
        requestService.executeRequests();
    }
}

package faang.school.accountservice.scheduling;

import faang.school.accountservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestScheduling {
    private final RequestService requestService;

    @Async
    @Scheduled(cron = "${spring.scheduling.cron}")
    public void executeRequest() {
        requestService.executeRequests();
    }
}

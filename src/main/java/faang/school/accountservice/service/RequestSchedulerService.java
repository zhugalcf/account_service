package faang.school.accountservice.service;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.executors.ThreadPoolProvider;
import faang.school.accountservice.repository.RequestRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RequestSchedulerService {
    private final RequestExecutorService requestExecutorService;
    private final RequestRepository requestRepository;
    private final List<ThreadPoolProvider> threadPoolProviders;

    @PostConstruct
    public void scheduleRequests() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::processScheduledRequests, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void processScheduledRequests() {
        List<Request> requestsToExecute = getRequestsToExecute();
        for (Request request : requestsToExecute) {
            Optional<ThreadPoolProvider> provider = threadPoolProviders.stream().filter(thread -> thread.getType().equals(request.getType())).findFirst();
            if (provider.isPresent()) {
                ThreadPoolTaskExecutor executor = provider.get().getThreadPool();
                executor.execute(() -> requestExecutorService.execute(request.getIdempotentToken()));
            }
        }
    }

    private List<Request> getRequestsToExecute() {
        return requestRepository.findIdempotentTokensForRetryRequestsToExecute();
    }
}
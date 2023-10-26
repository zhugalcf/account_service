package faang.school.accountservice.service;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;
import faang.school.accountservice.enums.RequestStatus;
import faang.school.accountservice.exception.NotFoundException;
import faang.school.accountservice.handlers.RequestTaskHandler;
import faang.school.accountservice.model.RequestTask;
import faang.school.accountservice.repository.RequestRepository;
import faang.school.accountservice.repository.RequestTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestExecutorService {
    private final List<RequestTaskHandler<String, Object>> handlers;
    private final RequestRepository requestRepository;
    private final RequestTaskRepository requestTaskRepository;

    public void execute(UUID requestId) {
        Request request = (Request) requestRepository.findByIdempotentToken(requestId).orElseThrow(() -> new NotFoundException("Request with ID " + requestId + " does not exist"));
        Optional<List<RequestTask>> requestTasksOptional = requestTaskRepository.findAllByRequest_IdempotentToken(requestId);
        List<RequestTask> requestTasks = requestTasksOptional.orElseThrow(() -> new NotFoundException("Request with ID " + requestId + " does not have request tasks"));

        List<RequestTaskHandler<String, Object>> filteredHandlers = handlers.stream()
                .filter(handler -> Arrays.stream(RequestHandler.values())
                        .anyMatch(requestHandler -> requestHandler == handler.getHandlerId()))
                .collect(Collectors.toList());

        Map<String, Object> contextMap = new HashMap<>();
        for (RequestHandler requestHandler : RequestHandler.values()) {
            RequestTaskHandler<String, Object> handler = handlers.stream()
                    .filter(h -> h.getHandlerId() == requestHandler)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Request with ID " + requestId + "does not have all need handlers"));
            Optional<RequestTask> requestTask = requestTasks.stream().filter(task -> task.getHandler() == requestHandler).findFirst();
            if (requestTask.isEmpty()) {
                throw new NotFoundException("Request with ID " + requestId + "does not have request task: " + requestHandler);
            }
            RequestTask task = requestTask.get();
            if (task.getStatus() == RequestStatus.EXECUTED) {
                continue;
            }
            try {
                handler.execute(request, contextMap);
                if(RequestHandler.SEND_NOTIFICATION == requestHandler){
                    request.setStatus(RequestStatus.EXECUTED);
                } else {
                    request.setStatus(RequestStatus.TODO);
                }
                task.setStatus(RequestStatus.EXECUTED);
            } catch (Exception e) {
                Integer retryCount = (Integer) contextMap.get("retry");
                if (retryCount != null) {
                    retryCount++;
                } else {
                    retryCount = 1;
                }
                contextMap.put("retry", retryCount);
                if (retryCount == 5) {
                    request.setStatus(RequestStatus.CANCELLED);
                    task.setStatus(RequestStatus.CANCELLED);
                } else {
                    request.setStatus(RequestStatus.TO_RETRY);
                    task.setStatus(RequestStatus.TO_RETRY);
                }
                return;
            } finally {
                request.setContext(contextMap.toString());
                requestRepository.save(request);
                requestTaskRepository.save(task);
            }
        }
    }
}
package faang.school.accountservice.service;

import faang.school.accountservice.entity.Request;
import faang.school.accountservice.enums.RequestHandler;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestExecutorService {
    private final List<RequestTaskHandler<String, Object>> handlers;
    private final RequestRepository requestRepository;
    private final RequestTaskRepository requestTaskRepository;

    public void execute(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request with ID " + requestId + " does not exist"));
        Optional<List<RequestTask>> requestTasksOptional = requestTaskRepository.findAllByRequestId(requestId);
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

            handler.execute(request, contextMap);


            //сохранять изм в таблицу и обработчки ошибок и ретрай и балбалаблаа
            // до дейлика успеть сдлеать задачи по ньюс фид
            // концептуально доделай уже без тестов основной функицонл
        }
    }
}

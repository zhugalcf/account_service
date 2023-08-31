package faang.school.accountservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventService {

    private final List<EventPublisher> publishers;

    public void publish(Object entity, Class<?> classOfEvent) {
        publishers.stream()
                .filter(eventPublisher -> eventPublisher.getEventType().equals(classOfEvent))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No publisher for type: %s", classOfEvent)))
                .send(entity);
    }
}

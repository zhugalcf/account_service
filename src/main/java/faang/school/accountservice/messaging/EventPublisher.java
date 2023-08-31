package faang.school.accountservice.messaging;

public interface EventPublisher<T> {

    Class<?> getEventType();

    void send(T event);
}

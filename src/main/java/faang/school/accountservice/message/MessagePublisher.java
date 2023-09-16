package faang.school.accountservice.message;

public interface MessagePublisher<T> {
    void publish(T message);
}
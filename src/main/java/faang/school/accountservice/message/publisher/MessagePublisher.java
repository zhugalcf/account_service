package faang.school.accountservice.message.publisher;

public interface MessagePublisher<T> {
    void publish(T message);
}
package faang.school.accountservice.exception;

public class TooManyAttemptsException extends RuntimeException{
    public TooManyAttemptsException(String message) {
        super(message);
    }
}

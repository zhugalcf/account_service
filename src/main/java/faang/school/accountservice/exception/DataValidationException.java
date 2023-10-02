package faang.school.accountservice.exception;

public class DataValidationException extends RuntimeException {
    public DataValidationException(String msg) {
        super(msg);
    }

    public DataValidationException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}

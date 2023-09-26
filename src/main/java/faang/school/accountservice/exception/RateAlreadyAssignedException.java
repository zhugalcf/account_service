package faang.school.accountservice.exception;

public class RateAlreadyAssignedException extends RuntimeException {
    public RateAlreadyAssignedException(String message) {
        super(message);
    }
}

package faang.school.accountservice.exception;

public class DuplicateBalanceException extends RuntimeException {
    public DuplicateBalanceException(String message) {
        super(message);
    }
}

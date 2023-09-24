package faang.school.accountservice.excpetion;

public class DuplicateBalanceException extends RuntimeException {
    public DuplicateBalanceException(String message) {
        super(message);
    }
}

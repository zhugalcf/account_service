package faang.school.accountservice.excpetion;

public class BalanceUpdateException extends RuntimeException {
    public BalanceUpdateException(String message) {
        super(message);
    }
}

package faang.school.accountservice.exception;

public class NoFreeAccountNumbersException extends RuntimeException {

    public NoFreeAccountNumbersException(String message) {
        super(message);
    }
}

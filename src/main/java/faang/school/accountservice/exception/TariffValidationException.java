package faang.school.accountservice.exception;

public class TariffValidationException extends RuntimeException {
    public TariffValidationException(String message) {
        super(message);
    }
}
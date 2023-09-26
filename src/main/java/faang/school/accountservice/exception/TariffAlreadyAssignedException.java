package faang.school.accountservice.exception;

public class TariffAlreadyAssignedException extends RuntimeException {
    public TariffAlreadyAssignedException(String message) {
        super(message);
    }
}

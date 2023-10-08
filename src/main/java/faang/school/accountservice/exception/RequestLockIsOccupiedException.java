package faang.school.accountservice.exception;

import jakarta.persistence.PersistenceException;

public class RequestLockIsOccupiedException extends PersistenceException {
    public RequestLockIsOccupiedException(String message) {
        super(message);
    }
}

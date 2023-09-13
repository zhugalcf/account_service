package faang.school.accountservice.exception;

import jakarta.persistence.EntityNotFoundException;

public class RequestNotFoundException extends EntityNotFoundException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}

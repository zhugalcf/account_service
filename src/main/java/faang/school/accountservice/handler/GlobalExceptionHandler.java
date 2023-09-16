package faang.school.accountservice.handler;

import faang.school.accountservice.exception.DataValidationException;
import faang.school.accountservice.exception.IdempotencyException;
import faang.school.accountservice.exception.LockedRequestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<String> handleDataValidationException(DataValidationException e) {
        log.error("Data validation exception occurred", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(IdempotencyException.class)
    public ResponseEntity<String> handleIdempotencyException(IdempotencyException e) {
        log.warn("IdempotencyException", e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(LockedRequestException.class)
    public ResponseEntity<String> handleLockedRequestException(LockedRequestException e) {
        log.warn("LockedRequestException", e);
        return ResponseEntity
                .status(HttpStatus.LOCKED)
                .body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found exception occurred", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleBusinessException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}

package faang.school.accountservice.exception.handler;

import faang.school.accountservice.exception.DataValidationException;
import faang.school.accountservice.exception.IdempotencyException;
import faang.school.accountservice.exception.LockedRequestException;
import faang.school.accountservice.exception.RateAlreadyAssignedException;
import faang.school.accountservice.exception.TariffAlreadyAssignedException;
import faang.school.accountservice.exception.TariffNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found exception occurred", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(constructMethodArgumentNotValidMessage(exception));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Map<String, String> body = Map.of("message", exception.getMessage());
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(RateAlreadyAssignedException.class)
    public ResponseEntity<Object> handleRateAlreadyAssignedException(RateAlreadyAssignedException exception) {
        Map<String, String> body = Map.of("message", exception.getMessage());
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(TariffAlreadyAssignedException.class)
    public ResponseEntity<Object> handleTariffAlreadyAssignedException(TariffAlreadyAssignedException exception) {
        Map<String, String> body = Map.of("message", exception.getMessage());
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(TariffNotFoundException.class)
    public ResponseEntity<Object> handleTariffNotFoundException(TariffNotFoundException exception) {
        Map<String, String> body = Map.of("message", exception.getMessage());
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleBusinessException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    private String constructMethodArgumentNotValidMessage(MethodArgumentNotValidException exception) {
        StringBuilder builder = new StringBuilder();
        exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .forEach(line -> builder.append(line).append("\n"));
        return builder.toString();
    }
}

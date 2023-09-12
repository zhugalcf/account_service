package faang.school.accountservice.controller;

import faang.school.accountservice.dto.GlobalExceptionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleBusinessException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GlobalExceptionDto> entityNotFoundException(EntityNotFoundException e) {
        log.error("The object was not found in the database: {}", e.toString());
        return new ResponseEntity<>(new GlobalExceptionDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalExceptionDto> illegalArgumentException(IllegalArgumentException e) {
        log.error("IncorrectInputDataException: {}", e.toString());
        return new ResponseEntity<>(new GlobalExceptionDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}

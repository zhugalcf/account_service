package faang.school.accountservice.controller;

import faang.school.accountservice.dto.ErrorResponseDto;
import faang.school.accountservice.exception.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GlobalExceptionHandlerTest {
    @Spy
    private GlobalExceptionHandler handler;

    @Test
    void handleMethodArgumentNotValidException_shouldReturnErrorResponse() {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.getExecutable()).thenReturn(method);

        FieldError fieldError = mock(FieldError.class);
        when(fieldError.getField()).thenReturn("field");
        when(fieldError.getDefaultMessage()).thenReturn("Field error message");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter,
                bindingResult);

        Map<String, String> errorResponseMap = handler.handleMethodArgumentNotValidException(exception);

        assertAll(() -> {
            assertEquals(1, errorResponseMap.size());
            assertEquals("field", errorResponseMap.keySet().stream().findFirst().orElse(""));
            assertEquals("Field error message", errorResponseMap.get("field"));
        });
    }

    @Test
    void handleConstraintViolationException_shouldReturnErrorResponse() {
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        when(exception.getMessage()).thenReturn("Constraint violation exception");

        ErrorResponseDto response = handler.handleConstraintViolationException(exception);

        assertAll(() -> {
            assertEquals("Constraint violation exception", response.getError());
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        });
    }

    @Test
    void handleEntityNotFoundException_shouldReturnErrorResponse() {
        EntityNotFoundException exception = mock(EntityNotFoundException.class);
        when(exception.getMessage()).thenReturn("Entity not found exception");

        ErrorResponseDto response = handler.handleEntityNotFoundException(exception);

        assertAll(() -> {
            assertEquals("Entity not found exception", response.getError());
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        });
    }

    @Test
    void handleException_shouldReturnErrorResponse() {
        Exception exception = mock(Exception.class);
        when(exception.getMessage()).thenReturn("Exception");

        ErrorResponseDto response = handler.handleException(exception);

        assertAll(() -> {
            assertEquals("Internal server error", response.getError());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        });
    }
}
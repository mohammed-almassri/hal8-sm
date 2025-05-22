package me.hal8.sm.posts.exception;

import me.hal8.sm.posts.dto.ErrorDTO;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneralException(Exception ex) {
        HttpStatus status = resolveResponseStatus(ex);
        ex.printStackTrace();
        ErrorDTO error = new ErrorDTO(
                ex.getMessage() != null ? ex.getMessage() : "Unexpected error"
        );

        return ResponseEntity.status(status).body(error);
    }

    private HttpStatus resolveResponseStatus(Exception ex) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.code(); // or responseStatus.value()
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

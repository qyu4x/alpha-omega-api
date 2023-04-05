package com.alphaomega.alphaomegarestfulapi.exception;

import com.alphaomega.alphaomegarestfulapi.payload.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CentralizedException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse<List<String>>> handleValidationErrors(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        ExceptionResponse<List<String>> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);

    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ExceptionResponse<String>> handleDataNotFound(DataNotFoundException exception) {
        ExceptionResponse<String> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse<String>> handleDataAlreadyExists(DataAlreadyExistsException exception) {
        ExceptionResponse<String> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    @ExceptionHandler(DataNotValidException.class)
    public ResponseEntity<ExceptionResponse<String>> handleDataNotValid(DataNotValidException exception) {
        ExceptionResponse<String> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

}

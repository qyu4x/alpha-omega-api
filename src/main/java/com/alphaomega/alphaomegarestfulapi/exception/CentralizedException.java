package com.alphaomega.alphaomegarestfulapi.exception;

import com.alphaomega.alphaomegarestfulapi.payload.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse<String>> handleDataNotValid(BadCredentialsException exception) {
        ExceptionResponse<String> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ExceptionResponse<String>> handleOtpInvalid(InvalidOtpException exception) {
        ExceptionResponse<String> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ExceptionResponse<String>> handleFileUploadErrors(FileUploadException exception) {
        ExceptionResponse<String> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
    @ExceptionHandler(FailedUploadFileException.class)
    public ResponseEntity<ExceptionResponse<String>> handleFailedUploadFile(FailedUploadFileException exception) {
        ExceptionResponse<String> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse<String>> handleValidation(BadRequestException exception) {
        ExceptionResponse<String> exceptionResponse = new ExceptionResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }



}

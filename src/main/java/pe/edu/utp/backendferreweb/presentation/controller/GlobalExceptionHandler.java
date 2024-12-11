package pe.edu.utp.backendferreweb.presentation.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import org.hibernate.internal.util.config.ConfigurationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.utp.backendferreweb.exceptions.FileUploadException;
import pe.edu.utp.backendferreweb.exceptions.IllegalOperationException;
import pe.edu.utp.backendferreweb.presentation.dto.response.ErrorResponse;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("A-001")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("A-002")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleAuthService(InternalAuthenticationServiceException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("A-003")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("E-001")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExists(EntityExistsException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("E-002")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArguments(IllegalArgumentException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("E-003")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder errors = new StringBuilder();

        for (ObjectError error : result.getAllErrors()) {
            errors.append(error.getDefaultMessage()).append("\n");
        }

        ErrorResponse error = ErrorResponse.builder()
                .code("E-003")
                .message(errors.toString())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleConfigurationException(ConfigurationException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("C-001")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFound(FileNotFoundException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("C-002")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("C-003")
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("C-004")
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("I-001")
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalOperationException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("I-002")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("I-002")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ErrorResponse> handleMailSend(MailSendException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("M-001")
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse error = ErrorResponse.builder()
                .code("G-001")
                .message("Ha ocurrido un error desconocido.")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

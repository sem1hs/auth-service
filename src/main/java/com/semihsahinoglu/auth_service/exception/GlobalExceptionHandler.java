package com.semihsahinoglu.auth_service.exception;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpServletRequest httpServletRequest =
                ((ServletWebRequest) request).getRequest();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getFieldError().getDefaultMessage(),
                httpServletRequest.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {

        HttpServletRequest httpServletRequest =
                ((ServletWebRequest) request).getRequest();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Kullanıcı zaten kayıtlı !",
                httpServletRequest.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthorityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorityNotFoundException(AuthorityNotFoundException ex, WebRequest request) {

        HttpServletRequest httpServletRequest =
                ((ServletWebRequest) request).getRequest();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                httpServletRequest.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationFailException.class)
    ResponseEntity<ErrorResponse> handleAuthenticationFailException(AuthenticationFailException ex, WebRequest request) {

        HttpServletRequest httpServletRequest =
                ((ServletWebRequest) request).getRequest();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                httpServletRequest.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(SignatureException.class)
    ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex, WebRequest request) {

        HttpServletRequest httpServletRequest =
                ((ServletWebRequest) request).getRequest();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Geçersiz Jwt Token",
                httpServletRequest.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

}

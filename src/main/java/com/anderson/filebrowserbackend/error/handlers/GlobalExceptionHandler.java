package com.anderson.filebrowserbackend.error.handlers;

import com.anderson.filebrowserbackend.error.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(HttpServletRequest request,
                                                                           BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                ApiError.builder()
                        .method(request.getMethod())
                        .path(request.getRequestURI())
                        .message("Validation failed for one or more fields. Please check the provided information.")
                        .timestamp(LocalDateTime.now())
                        .errors(errors)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> methodArgumentTypeMismatchExceptionHandler(HttpServletRequest request,
                                                                              MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.badRequest().body(
                ApiError.builder()
                        .method(request.getMethod())
                        .path(request.getRequestURI())
                        .message("Invalid value for the parameter " + exception.getName() + ".")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}

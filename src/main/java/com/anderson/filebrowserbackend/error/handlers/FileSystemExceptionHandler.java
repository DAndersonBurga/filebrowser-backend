package com.anderson.filebrowserbackend.error.handlers;

import com.anderson.filebrowserbackend.error.ApiError;
import com.anderson.filebrowserbackend.error.exceptions.FileNotFoundException;
import com.anderson.filebrowserbackend.error.exceptions.FileSourceInvalidException;
import com.anderson.filebrowserbackend.error.exceptions.VirtualDiskNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class FileSystemExceptionHandler {

    @ExceptionHandler({FileNotFoundException.class, VirtualDiskNotFoundException.class})
    public ResponseEntity<ApiError> fileNotFoundHandler(Exception exp, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiError.builder()
                    .message(exp.getMessage())
                    .path(request.getRequestURI())
                    .method(request.getMethod())
                    .timestamp(LocalDateTime.now())
                    .errors(Map.of())
                .build()
        );
    }

    @ExceptionHandler(FileSourceInvalidException.class)
    public ResponseEntity<ApiError> fileSourceInvalidHandler(FileSourceInvalidException exp, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiError.builder()
                    .message(exp.getMessage())
                    .path(request.getRequestURI())
                    .method(request.getMethod())
                    .timestamp(LocalDateTime.now())
                    .errors(Map.of())
                .build()
        );
    }

}

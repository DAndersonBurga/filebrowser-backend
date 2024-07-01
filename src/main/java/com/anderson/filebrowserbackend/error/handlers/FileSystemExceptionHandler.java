package com.anderson.filebrowserbackend.error.handlers;

import com.anderson.filebrowserbackend.error.ApiError;
import com.anderson.filebrowserbackend.error.exceptions.*;
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
    public ResponseEntity<ApiError> fileSourceInvalidExceptionHandler(FileSourceInvalidException exp, HttpServletRequest request) {
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

    @ExceptionHandler(DuplicateFileNameException.class)
    public ResponseEntity<ApiError> duplicateFileNameExceptionHandler(DuplicateFileNameException exp, HttpServletRequest request) {
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

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiError> fileUploadExceptionHandler(FileUploadException exp, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiError.builder()
                    .message(exp.getMessage())
                    .path(request.getRequestURI())
                    .method(request.getMethod())
                    .timestamp(LocalDateTime.now())
                    .errors(Map.of())
                .build()
        );
    }

    @ExceptionHandler(FileUploadedEmptyException.class)
    public ResponseEntity<ApiError> fileUploadedEmptyExceptionHandler(FileUploadedEmptyException exp, HttpServletRequest request) {
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

    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<ApiError> invalidFileFormatExceptionHandler(InvalidFileFormatException exp, HttpServletRequest request) {
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

    @ExceptionHandler(FileExportException.class)
    public ResponseEntity<ApiError> fileExportExceptionHandler(FileExportException exp, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
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

package com.anderson.filebrowserbackend.error.exceptions;

public class FileUploadedEmptyException extends RuntimeException {
    public FileUploadedEmptyException(String message) {
        super(message);
    }
}

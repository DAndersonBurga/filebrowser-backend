package com.anderson.filebrowserbackend.error.exceptions;

public class DuplicateFileNameException extends RuntimeException {
    public DuplicateFileNameException(String message) {
        super(message);
    }
}

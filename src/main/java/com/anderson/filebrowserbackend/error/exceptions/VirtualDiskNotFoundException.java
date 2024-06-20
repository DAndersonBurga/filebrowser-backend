package com.anderson.filebrowserbackend.error.exceptions;


public class VirtualDiskNotFoundException extends RuntimeException {
    public VirtualDiskNotFoundException(String message) {
        super(message);
    }
}

package com.anderson.filebrowserbackend.error;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ApiError {
    private String message;
    private String path;
    private String method;
    private LocalDateTime timestamp;
    private Map<String, String> errors;
}

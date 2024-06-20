package com.anderson.filebrowserbackend.controller.response;

import com.anderson.filebrowserbackend.model.FileType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FileResponse {
    private UUID id;
    private String name;
    private String description;
    private String path;
    private double size;
    private LocalDateTime creationAt;
    private LocalDateTime lastModifiedAt;
    private FileType fileType;
    private String content;
}

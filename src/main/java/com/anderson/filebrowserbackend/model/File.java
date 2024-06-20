package com.anderson.filebrowserbackend.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class File {
    private UUID id;
    private String name;
    private String path;
    private String description;
    private LocalDateTime creationAt;
    private LocalDateTime lastModifiedAt;
    private double size;
    private FileType fileType;
    private Map<String, File> files;
}
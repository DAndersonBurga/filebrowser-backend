package com.anderson.filebrowserbackend.domain.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public abstract class File {
    private String name;
    private String path;
    private String description;
    private LocalDateTime creationAt;
    private LocalDateTime lastModifiedAt;
    private double size;
    private FileType fileType;

}

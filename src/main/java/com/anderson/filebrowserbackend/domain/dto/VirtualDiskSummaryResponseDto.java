package com.anderson.filebrowserbackend.domain.dto;

import com.anderson.filebrowserbackend.domain.model.FileType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VirtualDiskSummaryResponseDto {

    private String label;
    private String name;
    private String description;
    private String path;
    private double size;
    private LocalDateTime creationAt;
    private LocalDateTime lastModifiedAt;
    private FileType fileType;
}

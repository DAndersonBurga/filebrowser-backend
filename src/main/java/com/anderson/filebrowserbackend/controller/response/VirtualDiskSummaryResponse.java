package com.anderson.filebrowserbackend.controller.response;

import com.anderson.filebrowserbackend.model.FileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirtualDiskSummaryResponse {

    private UUID id;
    private String label;
    private String name;
    private String description;
    private String path;
    private double size;
    private LocalDateTime creationAt;
    private LocalDateTime lastModifiedAt;
    private FileType fileType;

}

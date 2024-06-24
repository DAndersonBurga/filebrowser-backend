package com.anderson.filebrowserbackend.controller.response;

import com.anderson.filebrowserbackend.model.FileType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileActionResponse {
    private String message;
    private FileType fileType;
}

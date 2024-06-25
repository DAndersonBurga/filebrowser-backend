package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUpdateRequest {
    @NotBlank
    private String name;

    private String content;

    @NotBlank
    private String description;
}

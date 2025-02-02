package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUpdateRequest {
    @NotBlank
    private String name;

    private String content;

    @NotBlank
    private String description;
}

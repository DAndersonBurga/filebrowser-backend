package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class FileSystemCreateRequest {

    @NotBlank
    private String name;
}

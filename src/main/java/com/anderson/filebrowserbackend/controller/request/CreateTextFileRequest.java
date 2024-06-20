package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateTextFileRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    String description;

    @NotBlank(message = "El contenido no puede estar vacío")
    String content;
}

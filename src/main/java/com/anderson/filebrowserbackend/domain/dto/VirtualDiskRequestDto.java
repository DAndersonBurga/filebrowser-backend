package com.anderson.filebrowserbackend.domain.dto;

import com.anderson.filebrowserbackend.domain.model.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VirtualDiskRequestDto(

        @NotBlank(message = "La etiqueta no puede estar vacía o nula")
        @Pattern(message = "Solo esta permitido valores de a-z, A-Z", regexp = "^[A-Za-z]+$")
        String label,

        @NotBlank(message = "El nombre no puede estar vacío o nulo")
        String name,

        String description,

        @NotNull(message = "El tipo de archivo no puede ser nulo")
        FileType filetype
) {
}

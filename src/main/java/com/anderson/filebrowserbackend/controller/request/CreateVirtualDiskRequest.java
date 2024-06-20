package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
public class CreateVirtualDiskRequest {
        @NotBlank(message = "La etiqueta no puede estar vacía o nula")
        @Pattern(regexp = "^[A-Za-z]+$", message = "Solo esta permitido valores de a-z, A-Z")
        @Length(max = 1, min = 1, message = "La etiqueta debe tener un solo carácter")
        private String label;

        @NotBlank(message = "El nombre no puede estar vacío o nulo")
        private String name;

        private String description;
}

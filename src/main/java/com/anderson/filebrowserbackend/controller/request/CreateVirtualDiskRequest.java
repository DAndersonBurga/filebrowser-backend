package com.anderson.filebrowserbackend.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @JsonProperty("label")
        @Schema(example = "A", description = "")
        private String label;

        @NotBlank(message = "El nombre no puede estar vacío o nulo")
        @JsonProperty("name")
        @Schema(example = "Mi disco Virtual A")
        private String name;

        @JsonProperty("description")
        @Schema(example = "Descripción de mi disco A")
        private String description;
}

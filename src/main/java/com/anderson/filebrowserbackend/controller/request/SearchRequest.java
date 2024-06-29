package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SearchRequest {

    @NotNull
    private UUID diskId;
    @NotNull
    private UUID parentId;

    @NotBlank
    private String query;
}

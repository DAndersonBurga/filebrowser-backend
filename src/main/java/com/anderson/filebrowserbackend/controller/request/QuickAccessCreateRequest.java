package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class QuickAccessCreateRequest {

    @NotNull
    private UUID diskId;

    @NotNull
    private UUID fileId;
}

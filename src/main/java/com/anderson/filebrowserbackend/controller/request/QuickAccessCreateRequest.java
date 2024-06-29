package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuickAccessCreateRequest {

    @NotNull
    private UUID diskId;

    @NotNull
    private UUID fileId;
}

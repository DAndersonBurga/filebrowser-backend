package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class FileActionRequest {

    @NotNull
    private UUID sourceDiskId;
    @NotNull
    private UUID sourceParentId;
    @NotNull
    private UUID fileId;

    @NotNull
    private UUID destinationDiskId;
    @NotNull
    private UUID destinationParentId;
}

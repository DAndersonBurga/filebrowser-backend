package com.anderson.filebrowserbackend.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class FileCutRequest {

    @NotNull
    private UUID sourceDiskId;
    @NotNull
    private UUID sourceParentId;
    @NotNull
    private UUID sourceFileId;

    @NotNull
    private UUID destinationDiskId;
    @NotNull
    private UUID destinationParentId;
    @NotNull
    private UUID destinationFileId;
}

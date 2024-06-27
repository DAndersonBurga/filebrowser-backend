package com.anderson.filebrowserbackend.controller.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DirectorySearchResponse {
    private UUID diskId;
    private UUID directoryId;
}

package com.anderson.filebrowserbackend.controller.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectorySearchResponse {
    private UUID diskId;
    private UUID directoryId;
}

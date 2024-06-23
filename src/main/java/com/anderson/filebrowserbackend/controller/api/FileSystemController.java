package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.service.interfaces.FileSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/file-system")
@RequiredArgsConstructor
@Tag(name = "File System", description = "Controlador para crear un sistema de archivos y listar los discos virtuales del sistema.")
public class FileSystemController {

    private final FileSystemService fileSystemService;

    @GetMapping
    @Operation(
        summary = "Listar discos virtuales",
        description = "Listar los discos virtuales del sistema.",
        tags = {"File System"}
    )
    public ResponseEntity<List<VirtualDiskSummaryResponse>> getFileSystem() {
        return ResponseEntity.ok(fileSystemService.getVirtualDisks());
    }

}

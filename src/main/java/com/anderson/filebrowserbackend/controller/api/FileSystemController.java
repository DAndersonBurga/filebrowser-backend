package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.request.FileSystemCreateRequest;
import com.anderson.filebrowserbackend.controller.response.CreatedFileSystemResponse;
import com.anderson.filebrowserbackend.controller.response.FileSystemUploadResponse;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.model.FileSystem;
import com.anderson.filebrowserbackend.service.interfaces.FileSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file-system")
@RequiredArgsConstructor
@Tag(name = "File System", description = "Controlador para crear un sistema de archivos y listar los discos virtuales del sistema.")
public class FileSystemController {

    private final FileSystemService fileSystemService;

    @PostMapping
    public ResponseEntity<CreatedFileSystemResponse> createFileSystem(@RequestBody @Valid FileSystemCreateRequest request) {
        return ResponseEntity.ok(fileSystemService.create(request));
    }

    @PostMapping("/upload")
    public ResponseEntity<FileSystemUploadResponse> uploadFileSystem(@RequestPart(name = "file") MultipartFile multipartFile) {

        return ResponseEntity.ok(fileSystemService.upload(multipartFile));
    }

    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportFileSystem() {

        ByteArrayResource arrayResource = fileSystemService.export();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filesystem.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(arrayResource.getByteArray().length)
                .body(arrayResource);
    }

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

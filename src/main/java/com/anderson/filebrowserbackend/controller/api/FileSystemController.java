package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.request.FileSystemCreateRequest;
import com.anderson.filebrowserbackend.controller.response.CreatedFileSystemResponse;
import com.anderson.filebrowserbackend.controller.response.FileSystemUploadResponse;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.service.interfaces.FileSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/download/{disk-id}/{file-id}")
    public ResponseEntity<ByteArrayResource> exportFileSystem(
            @PathVariable(name = "disk-id") UUID diskId,
            @PathVariable(name = "file-id") UUID fileId) throws IOException {

        ByteArrayResource fileSystemResource = fileSystemService.downloadFile(diskId, fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileSystemResource.getFilename() + "\"")
                .contentLength(fileSystemResource.contentLength())
                .body(fileSystemResource);
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

package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.request.CreateVirtualDiskRequest;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.error.ApiError;
import com.anderson.filebrowserbackend.service.interfaces.VirtualDiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/disk")
@RequiredArgsConstructor
@Tag(name = "Disk", description = "Operaciones con discos virtuales (solo crear y listar el primer nivel)")
public class DiskController {

    private final VirtualDiskService virtualDiskService;

    @GetMapping("/{id}")
    @Operation(
        summary = "Trae todos los archivos de un disco virtual (no devuelve los archivos dentro de directorios)",
        description = "Trae solo el primer nivel de archivos del disco virtual",
        tags = {"Disk"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de archivos",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        type = "array",
                        implementation = FileResponse.class
                    )
                )
            )
        }
    )
    public ResponseEntity<List<FileResponse>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(virtualDiskService.findFiles(id));
    }

    @PostMapping
    @Operation(
        summary = "Crear un disco virtual",
        description = "Crea un disco virtual con los datos proporcionados",
        tags = {"Disk"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CreateVirtualDiskRequest.class)
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Disco virtual creado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = VirtualDiskSummaryResponse.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error en los datos proporcionados",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    type = "object",
                    implementation = ApiError.class
                )
            )
        )
    })
    public ResponseEntity<VirtualDiskSummaryResponse> create(@RequestBody @Valid CreateVirtualDiskRequest request) {
        return ResponseEntity.ok(virtualDiskService.create(request));
    }
}

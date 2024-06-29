package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.controller.request.FileSystemCreateRequest;
import com.anderson.filebrowserbackend.controller.response.CreatedFileSystemResponse;
import com.anderson.filebrowserbackend.controller.response.FileSystemUploadResponse;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.error.exceptions.FileExportException;
import com.anderson.filebrowserbackend.error.exceptions.FileUploadedEmptyException;
import com.anderson.filebrowserbackend.error.exceptions.InvalidFileFormatException;
import com.anderson.filebrowserbackend.model.FileSystem;
import com.anderson.filebrowserbackend.service.interfaces.FileSystemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileSystemServiceImpl implements FileSystemService {

    private final FileSystem fileSystem;
    private final ModelMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public List<VirtualDiskSummaryResponse> getVirtualDisks() {

        return fileSystem
                .getVirtualDisks().stream()
                .map(virtualDisk -> mapper.map(virtualDisk, VirtualDiskSummaryResponse.class))
                .toList();
    }

    @Override
    public CreatedFileSystemResponse create(FileSystemCreateRequest request) {

        fileSystem.setName(request.getName());
        fileSystem.setQuickAccessList(new ArrayList<>());
        fileSystem.setVirtualDisks(new ArrayList<>());

        return new CreatedFileSystemResponse("Sistema de archivos creado correctamente!");
    }

    @Override
    public FileSystemUploadResponse upload(MultipartFile multipartFile) {

        if( multipartFile.isEmpty() ) {
            throw new FileUploadedEmptyException("Archivo vacío no permitido.");
        }

        if(multipartFile.getOriginalFilename() == null || !multipartFile.getOriginalFilename().endsWith(".json")) {
            throw new InvalidFileFormatException("Formato de archivo inválido. Solo se permiten archivos .json");
        }

        // Convert file in FileSystem (Object)
        try {
            FileSystem fileSystemUploaded = objectMapper.readValue(multipartFile.getInputStream(), FileSystem.class);
            fileSystem.setName(fileSystemUploaded.getName());
            fileSystem.setVirtualDisks(fileSystemUploaded.getVirtualDisks());
            fileSystem.setQuickAccessList(fileSystemUploaded.getQuickAccessList());

            return new FileSystemUploadResponse(fileSystem.getName());

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new FileUploadedEmptyException("Error al leer el archivo.");
        }

    }

    @Override
    public ByteArrayResource export() {
        try {
            String jsonString = objectMapper.writeValueAsString(fileSystem);
            byte[] jsonBytes = jsonString.getBytes();

            return new ByteArrayResource(jsonBytes);

        } catch (JsonProcessingException e) {
            throw new FileExportException("Error al exportar el archivo.");
        }
    }
}

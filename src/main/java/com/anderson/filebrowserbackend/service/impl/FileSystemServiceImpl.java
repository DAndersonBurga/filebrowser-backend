package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.controller.request.FileSystemCreateRequest;
import com.anderson.filebrowserbackend.controller.response.CreatedFileSystemResponse;
import com.anderson.filebrowserbackend.controller.response.FileSystemUploadResponse;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.error.exceptions.FileExportException;
import com.anderson.filebrowserbackend.error.exceptions.FileUploadedEmptyException;
import com.anderson.filebrowserbackend.error.exceptions.InvalidFileFormatException;
import com.anderson.filebrowserbackend.error.exceptions.VirtualDiskNotFoundException;
import com.anderson.filebrowserbackend.model.*;
import com.anderson.filebrowserbackend.service.interfaces.FileSystemService;
import com.anderson.filebrowserbackend.utils.FileSystemUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class FileSystemServiceImpl implements FileSystemService {

    private final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    private final FileSystem fileSystem;
    private final ModelMapper mapper;
    private final ObjectMapper objectMapper;
    private final FileSystemUtils fileSystemUtils;

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

        if (multipartFile.isEmpty()) {
            throw new FileUploadedEmptyException("Archivo vacío no permitido.");
        }

        if (multipartFile.getOriginalFilename() == null || !multipartFile.getOriginalFilename().endsWith(".json")) {
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

    @Override
    public ByteArrayResource downloadFile(UUID diskId, UUID fileId) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(diskId)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        MyFile fileFound = fileSystemUtils.findFileById(virtualDisk, fileId)
                .orElseThrow(() -> new VirtualDiskNotFoundException("File not found"));

        try {
            if (fileFound.getFileType() == FileType.TXT_FILE) {
                File tempFile = File.createTempFile(fileFound.getName(), ".txt");
                createTxtFile(tempFile, (TextMyFile) fileFound);
                return new ByteArrayResource(Files.readAllBytes(tempFile.toPath()));
            } else if (fileFound.getFileType() == FileType.DIRECTORY) {
                File zipFile = createTempZipFile(fileFound);
                return new ByteArrayResource(Files.readAllBytes(zipFile.toPath()));
            } else {
                throw new IllegalArgumentException("Unsupported file type for download");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating or retrieving temporary file", e);
        }
    }

    private void createTxtFile(File file, TextMyFile textMyFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textMyFile.getContent());
        } catch (IOException e) {
            throw new RuntimeException("Error creating text file", e);
        }
    }

    public File createTempZipFile(MyFile directory) throws IOException {
        File zipFile = File.createTempFile(directory.getName(), ".zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            addFilesToZip(zos, Paths.get(directory.getName()), directory);
        }
        return zipFile;
    }

    private void addFilesToZip(ZipOutputStream zos, Path currentPath, MyFile directory) throws IOException {
        for (Map.Entry<String, MyFile> fileEntry : directory.getFiles().entrySet()) {
            MyFile file = fileEntry.getValue();
            Path filePath = currentPath.resolve(file.getName());

            if (file.getFileType() == FileType.TXT_FILE) {
                zos.putNextEntry(new ZipEntry(filePath + ".txt"));
                byte[] contentBytes = ((TextMyFile) file).getContent().getBytes(StandardCharsets.UTF_8);
                zos.write(contentBytes, 0, contentBytes.length);
                zos.closeEntry();

            } else if (file.getFileType() == FileType.DIRECTORY) {
                zos.putNextEntry(new ZipEntry(filePath + "/"));
                zos.closeEntry();

                addFilesToZip(zos, filePath, file);

            } else {
                throw new IllegalArgumentException("Unsupported file type for zip");
            }
        }
    }
}

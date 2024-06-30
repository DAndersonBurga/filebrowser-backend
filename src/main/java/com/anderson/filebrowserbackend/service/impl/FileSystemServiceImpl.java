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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    @Override
    public FileSystemResource downloadFile(UUID diskId, UUID fileId) {

        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(diskId)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        MyFile fileFound = fileSystemUtils.findFileById(virtualDisk, fileId)
                .orElseThrow(() -> new VirtualDiskNotFoundException("File not found"));

        if (fileFound.getFileType() == FileType.TXT_FILE) {
            File file = new File(TEMP_DIR, fileFound.getName() + ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                TextMyFile textMyFile = (TextMyFile) fileFound;
                writer.write(textMyFile.getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new FileSystemResource(file);
        }

        return null;
    }

//        try {
//            // Crear una carpeta temporal
//            Path tempDirPath = Paths.get(TEMP_DIR, "Carpeta1");
//            if (!Files.exists(tempDirPath)) {
//                Files.createDirectory(tempDirPath);
//            }
//
//            // Crear un archivo de texto dentro de la carpeta temporal
//            Path tempFilePath =  Files.createFile(tempDirPath.resolve("archivo.txt"));
//            try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath)) {
//                writer.write("Contenido del archivo");
//            }
//
//            Path zipFilePath = Paths.get(TEMP_DIR, "archivoZip.zip");
//
//            try (FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
//                 ZipOutputStream zos = new ZipOutputStream(fos)) {
//                zipDirectory(tempDirPath.toFile(), tempDirPath.getFileName().toString(), zos);
//            }
//
//            // Retornar la respuesta con el archivo ZIP de la carpeta temporal
//            return new FileSystemResource(zipFilePath.toFile());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//
//        }


    private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }
            zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }
            }
            zos.closeEntry();
        }
    }
}

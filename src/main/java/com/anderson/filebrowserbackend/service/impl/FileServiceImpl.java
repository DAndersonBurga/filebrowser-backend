package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.controller.request.CreateFolderRequest;
import com.anderson.filebrowserbackend.controller.request.CreateTextFileRequest;
import com.anderson.filebrowserbackend.controller.request.FileActionRequest;
import com.anderson.filebrowserbackend.controller.request.SearchRequest;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.error.exceptions.FileNotFoundException;
import com.anderson.filebrowserbackend.error.exceptions.FileSourceInvalidException;
import com.anderson.filebrowserbackend.error.exceptions.VirtualDiskNotFoundException;
import com.anderson.filebrowserbackend.model.*;
import com.anderson.filebrowserbackend.service.interfaces.FileService;
import com.anderson.filebrowserbackend.utils.FileSystemUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ModelMapper mapper;
    private final FileSystemUtils fileSystemUtils;

    @Override
    public FileActionResponse createFolder(UUID idDisk, UUID idParent, CreateFolderRequest request) {

        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(virtualDisk.getId().equals(idParent)) {
            return createFolderInParent(request, virtualDisk);
        }

        Directory parent = (Directory) fileSystemUtils.findFileById(virtualDisk, idParent)
                .orElseThrow(() -> new FileNotFoundException("Parent file not found"));

        return createFolderInParent(request, parent);
    }

    @Override
    public FileActionResponse createFile(UUID idDisk, UUID idParent, CreateTextFileRequest request) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(virtualDisk.getId().equals(idParent)) {
            return createTextFileInParent(request, virtualDisk);
        }

        Directory parent = (Directory) fileSystemUtils.findFileById(virtualDisk, idParent)
                .orElseThrow(() -> new FileNotFoundException("Parent file not found"));

        return createTextFileInParent(request, parent);
    }

    @Override
    public List<FileResponse> getAllFilesInADirectory(UUID idDisk, UUID idDirectory) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        Directory directory = (Directory) fileSystemUtils.findFileById(virtualDisk, idDirectory)
                .orElseThrow(() -> new FileNotFoundException("Directory not found"));

        List<FileResponse> fileResponses = new ArrayList<>();

        for (Map.Entry<String, File> stringFileEntry : directory.getFiles().entrySet()) {
            FileResponse fileResponse = mapper.map(stringFileEntry.getValue(), FileResponse.class);
            fileResponse.setPath(virtualDisk.getPath() + directory.getPath() + "/" + fileResponse.getName());
            fileResponses.add(fileResponse);
        }

        return fileResponses;
    }

    @Override
    public FileActionResponse copyFile(FileActionRequest fileActionRequest) {
        VirtualDisk sourceVirtualDisk = fileSystemUtils.findVirtualDisk(fileActionRequest.getSourceDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        File file = fileSystemUtils.findFileById(sourceVirtualDisk, fileActionRequest.getFileId())
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        File newFile = SerializationUtils.clone(file);

        VirtualDisk destinationVirtualDisk;

        if(fileActionRequest.getSourceDiskId() == fileActionRequest.getDestinationDiskId()) {
            destinationVirtualDisk = sourceVirtualDisk;
        } else {
            destinationVirtualDisk = fileSystemUtils.findVirtualDisk(fileActionRequest.getDestinationDiskId())
                    .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));
        }

        return copyFileInParent(destinationVirtualDisk, destinationVirtualDisk.getId(), fileActionRequest.getDestinationParentId(), newFile);
    }

    @Override
    public void deleteFile(UUID idDisk, UUID idParent, UUID idFile) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(idDisk.equals(idParent)) {
            virtualDisk.getFiles().remove(idFile.toString());
        } else {
            File parent = fileSystemUtils.findFileById(virtualDisk, idParent)
                    .orElseThrow(() -> new FileNotFoundException("Parent directory not found"));

            parent.getFiles().remove(idFile.toString());
        }
    }

    @Override
    public FileActionResponse cutFile(FileActionRequest fileActionRequest) {
        if(fileActionRequest.getDestinationDiskId().equals(fileActionRequest.getSourceDiskId()) &&
            fileActionRequest.getDestinationParentId().equals(fileActionRequest.getSourceParentId())) {
            throw new FileSourceInvalidException("El origen y el destino son los mismos");
        }

        VirtualDisk sourceVirtualDisk = fileSystemUtils.findVirtualDisk(fileActionRequest.getSourceDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        File file = fileSystemUtils.findFileById(sourceVirtualDisk, fileActionRequest.getFileId())
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        File newFile = SerializationUtils.clone(file);

        // Delete file in the source
        deleteFile(fileActionRequest.getSourceDiskId(),
                fileActionRequest.getSourceParentId(),
                fileActionRequest.getFileId());

        // Insert file in destination
        VirtualDisk destinationVirtualDisk = fileSystemUtils.findVirtualDisk(fileActionRequest.getDestinationDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(fileActionRequest.getDestinationDiskId().equals(fileActionRequest.getDestinationParentId())) {
            destinationVirtualDisk.getFiles().put(newFile.getId().toString(), newFile);
            return new FileActionResponse("Archivo cortado correctamente!", newFile.getFileType());
        }

        Directory parent = (Directory) fileSystemUtils.findFileById(
                    destinationVirtualDisk, fileActionRequest.getDestinationParentId()
                )
            .orElseThrow(() -> new FileNotFoundException("Parent directory not found"));

        parent.getFiles().put(newFile.getId().toString(), newFile);

        return new FileActionResponse("Archivo cortado correctamente!", newFile.getFileType());

    }

    @Override
    public List<FileResponse> search(SearchRequest searchRequest) {
        List<FileResponse> coincidences = new ArrayList<>();
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(searchRequest.getDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(searchRequest.getDiskId().equals(searchRequest.getParentId())) {
            fileSystemUtils.searchFilesByName(coincidences, searchRequest.getQuery(), virtualDisk, virtualDisk);
            return coincidences;
        }

        File parent = fileSystemUtils.findFileById(virtualDisk, searchRequest.getParentId())
                .orElseThrow(() -> new FileNotFoundException("Parent file not found"));

        fileSystemUtils.searchFilesByName(coincidences, searchRequest.getQuery(), parent, virtualDisk);

        return coincidences;
    }

    private FileActionResponse createTextFileInParent(CreateTextFileRequest request, File parent) {
        TextFile file = mapper.map(request, TextFile.class);
        file.setCreationAt(LocalDateTime.now());
        file.setId(UUID.randomUUID());
        file.setPath("");
        file.setFileType(FileType.TXT_FILE);
        file.setSize(0L);
        file.setFiles(new HashMap<>());

        parent.getFiles().put(file.getId().toString(), file);

        return new FileActionResponse("Folder created", file.getFileType());
    }

    private FileActionResponse createFolderInParent(CreateFolderRequest request, File parent) {

        Directory folder = mapper.map(request, Directory.class);
        folder.setCreationAt(LocalDateTime.now());
        folder.setId(UUID.randomUUID());

        if(parent.getFileType() == FileType.VIRTUAL_DISK) {
            folder.setPath("/" +  folder.getName());
        } else {
            folder.setPath(parent.getPath() + "/" + folder.getName());
        }

        folder.setFileType(FileType.DIRECTORY);
        folder.setSize(0L);
        folder.setFiles(new HashMap<>());

        parent.getFiles().put(folder.getId().toString(), folder);

        return new FileActionResponse("Folder created", folder.getFileType());
    }

    private FileActionResponse copyFileInParent(VirtualDisk virtualDisk, UUID idDisk, UUID idParent, File newFile) {
        UUID uuid = UUID.randomUUID();
        newFile.setName(newFile.getName() + "-copy" + uuid.toString().substring(0,5));
        newFile.setId(uuid);

        if(idDisk.equals(idParent)) {
            if(newFile.getFileType() == FileType.DIRECTORY) {
                newFile.setPath("/" + newFile.getName());
                Directory directory = (Directory) newFile;
                virtualDisk.getFiles().put(uuid.toString(), directory);
            }

            virtualDisk.getFiles().put(uuid.toString(), newFile);

            return new FileActionResponse("Archivo copiado correctamente!", newFile.getFileType());
        }

        Directory directory = (Directory) fileSystemUtils.findFileById(virtualDisk, idParent)
                .orElseThrow(() -> new FileNotFoundException("Directory not found"));

        if(newFile.getFileType() == FileType.DIRECTORY) {
            newFile.setPath(directory.getPath() + "/" + newFile.getName());
            Directory directoryFile = (Directory) newFile;
            virtualDisk.getFiles().put(uuid.toString(), directoryFile);
        }

        directory.getFiles().put(newFile.getId().toString(), newFile);

        return new FileActionResponse("Archivo copiado correctamente!", newFile.getFileType());
    }

}

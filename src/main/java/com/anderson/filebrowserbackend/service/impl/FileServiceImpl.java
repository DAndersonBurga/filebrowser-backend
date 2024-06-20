package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.controller.request.CreateFolderRequest;
import com.anderson.filebrowserbackend.controller.request.CreateTextFileRequest;
import com.anderson.filebrowserbackend.controller.response.FileCreatedResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.error.exceptions.FileNotFoundException;
import com.anderson.filebrowserbackend.error.exceptions.VirtualDiskNotFoundException;
import com.anderson.filebrowserbackend.model.*;
import com.anderson.filebrowserbackend.service.interfaces.FileService;
import com.anderson.filebrowserbackend.utils.FileSystemUtils;
import lombok.RequiredArgsConstructor;
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
    public FileCreatedResponse createFolder(UUID idDisk, UUID idParent, CreateFolderRequest request) {

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
    public FileCreatedResponse createFile(UUID idDisk, UUID idParent, CreateTextFileRequest request) {
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
            fileResponses.add(fileResponse);
        }

        return fileResponses;
    }

    private FileCreatedResponse createTextFileInParent(CreateTextFileRequest request, File parent) {
        TextFile file = mapper.map(request, TextFile.class);
        file.setCreationAt(LocalDateTime.now());
        file.setId(UUID.randomUUID());
        file.setPath("");
        file.setFileType(FileType.TXT_FILE);
        file.setSize(0L);
        file.setFiles(new HashMap<>());

        parent.getFiles().put(file.getId().toString(), file);

        return new FileCreatedResponse("Folder created", file.getFileType());
    }

    private FileCreatedResponse createFolderInParent(CreateFolderRequest request, File parent) {
        Directory folder = mapper.map(request, Directory.class);
        folder.setCreationAt(LocalDateTime.now());
        folder.setId(UUID.randomUUID());
        folder.setPath("/" +  folder.getName());
        folder.setFileType(FileType.DIRECTORY);
        folder.setSize(0L);
        folder.setFiles(new HashMap<>());

        parent.getFiles().put(folder.getId().toString(), folder);

        return new FileCreatedResponse("Folder created", folder.getFileType());
    }

}

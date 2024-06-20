package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.controller.request.CreateVirtualDiskRequest;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.model.*;
import com.anderson.filebrowserbackend.error.exceptions.VirtualDiskNotFoundException;
import com.anderson.filebrowserbackend.service.interfaces.VirtualDiskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VirtualDiskServiceImpl implements VirtualDiskService {

    private final FileSystem fileSystem;
    private final ModelMapper mapper;

    @Override
    public VirtualDiskSummaryResponse create(CreateVirtualDiskRequest request) {

        VirtualDisk virtualDisk = mapper.map(request, VirtualDisk.class);

        virtualDisk.setId(UUID.randomUUID());
        virtualDisk.setCreationAt(LocalDateTime.now());
        virtualDisk.setFiles(new HashMap<>());
        virtualDisk.setSize(0L);
        virtualDisk.setPath("/".concat(virtualDisk.getLabel()));
        virtualDisk.setFileType(FileType.VIRTUAL_DISK);

        fileSystem.getVirtualDisks().add(virtualDisk);

        return mapper.map(virtualDisk, VirtualDiskSummaryResponse.class);
    }

    @Override
    public List<FileResponse> findFiles(UUID id) {

        VirtualDisk virtualDisk = null;
        for (VirtualDisk disk : fileSystem.getVirtualDisks()) {
            if (disk.getId().equals(id)) {
                virtualDisk = disk;
                break;
            }
        }

        if (virtualDisk == null) {
            throw new VirtualDiskNotFoundException("Virtual disk not found.");
        }

        Map<String, File> files = virtualDisk.getFiles();
        List<FileResponse> fileResponses = new ArrayList<>();

        for (Map.Entry<String, File> stringFileEntry : files.entrySet()) {
            File file = stringFileEntry.getValue();
            FileResponse fileResponse;

            switch (file.getFileType()) {
                case DIRECTORY -> {
                    Directory directory = (Directory) file;
                    fileResponse = mapper.map(directory, FileResponse.class);
                    fileResponse.setPath(virtualDisk.getPath() + file.getPath());
                }
                case TXT_FILE -> {
                    TextFile textFile = (TextFile) file;
                    fileResponse = mapper.map(textFile, FileResponse.class);
                    fileResponse.setPath(virtualDisk.getPath() + file.getPath());
                }

                default -> fileResponse = null;
            }

            fileResponses.add(fileResponse);
        }


        return fileResponses;
    }
}

package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.controller.request.VirtualDiskCreateRequest;
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
    public VirtualDiskSummaryResponse create(VirtualDiskCreateRequest request) {

        VirtualDisk virtualDisk = mapper.map(request, VirtualDisk.class);

        virtualDisk.setId(UUID.randomUUID());
        virtualDisk.setCreationAt(LocalDateTime.now());
        virtualDisk.setFiles(new HashMap<>());
        virtualDisk.setPath("/".concat(virtualDisk.getLabel()));
        virtualDisk.setFileType(FileType.VIRTUAL_DISK);

        double size = ((double) request.getLabel().getBytes().length / 1024) +
                ((double) request.getName().getBytes().length / 1024);

        virtualDisk.setSize(size);

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

            fileResponse = mapper.map(file, FileResponse.class);
            if(file.getFileType() == FileType.TXT_FILE) {
                fileResponse.setPath(file.getPath());
            }

            fileResponses.add(fileResponse);
        }


        return fileResponses;
    }
}

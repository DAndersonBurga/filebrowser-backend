package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.controller.request.QuickAccessCreateRequest;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.error.exceptions.FileNotFoundException;
import com.anderson.filebrowserbackend.error.exceptions.VirtualDiskNotFoundException;
import com.anderson.filebrowserbackend.model.MyFile;
import com.anderson.filebrowserbackend.model.FileSystem;
import com.anderson.filebrowserbackend.model.VirtualDisk;
import com.anderson.filebrowserbackend.service.interfaces.QuickAccessService;
import com.anderson.filebrowserbackend.utils.FileSystemUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuickAccessServiceImpl implements QuickAccessService {

    private final FileSystem fileSystem;
    private final FileSystemUtils fileSystemUtils;
    private final ModelMapper mapper;

    @Override
    public FileActionResponse createQuickAccess(QuickAccessCreateRequest request) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(request.getDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        MyFile fileFound = fileSystemUtils.findFileById(virtualDisk, request.getFileId())
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        fileSystem.getQuickAccessList().add(fileFound);

        return new FileActionResponse("Acceso rápido creado!", fileFound.getFileType());
    }

    @Override
    public List<FileResponse> getQuickAccess() {

        List<FileResponse> responses = new ArrayList<>();
        for (MyFile file : fileSystem.getQuickAccessList()) {

            FileResponse fileResponse = mapper.map(file, FileResponse.class);
            responses.add(fileResponse);
        }

        return responses;
    }
}

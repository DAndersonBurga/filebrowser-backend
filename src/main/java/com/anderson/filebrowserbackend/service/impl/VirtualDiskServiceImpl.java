package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.domain.dto.VirtualDiskRequestDto;
import com.anderson.filebrowserbackend.domain.dto.VirtualDiskSummaryResponseDto;
import com.anderson.filebrowserbackend.domain.model.FileSystem;
import com.anderson.filebrowserbackend.domain.model.VirtualDisk;
import com.anderson.filebrowserbackend.mapper.VirtualDiskMapper;
import com.anderson.filebrowserbackend.service.interfaces.VirtualDiskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class VirtualDiskServiceImpl implements VirtualDiskService {

    private final FileSystem fileSystem;
    private final VirtualDiskMapper mapper;

    @Override
    public VirtualDiskSummaryResponseDto create(VirtualDiskRequestDto request) {

        VirtualDisk virtualDisk = mapper.createVirtualDisk(request);

        virtualDisk.setCreationAt(LocalDateTime.now());
        virtualDisk.setFiles(new HashMap<>());
        virtualDisk.setSize(0L);
        virtualDisk.setPath("/".concat(virtualDisk.getLabel()));

        fileSystem.getVirtualDisks().add(virtualDisk);

        return mapper.fromVirtualDisk(virtualDisk);
    }
}

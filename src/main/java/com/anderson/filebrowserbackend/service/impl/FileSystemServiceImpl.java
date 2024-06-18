package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.domain.dto.VirtualDiskSummaryResponseDto;
import com.anderson.filebrowserbackend.domain.model.FileSystem;
import com.anderson.filebrowserbackend.service.interfaces.FileSystemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileSystemServiceImpl implements FileSystemService {

    private final FileSystem fileSystem;
    private final ModelMapper mapper;

    @Override
    public List<VirtualDiskSummaryResponseDto> getVirtualDisks() {

        return fileSystem
                .getVirtualDisks().stream()
                .map(virtualDisk -> mapper.map(virtualDisk, VirtualDiskSummaryResponseDto.class))
                .toList();
    }
}

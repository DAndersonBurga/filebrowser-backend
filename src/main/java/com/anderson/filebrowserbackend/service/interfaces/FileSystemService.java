package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.domain.dto.VirtualDiskSummaryResponseDto;

import java.util.List;


public interface FileSystemService {
    List<VirtualDiskSummaryResponseDto> getVirtualDisks();
}

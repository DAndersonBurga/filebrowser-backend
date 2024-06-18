package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.domain.dto.VirtualDiskRequestDto;
import com.anderson.filebrowserbackend.domain.dto.VirtualDiskSummaryResponseDto;

public interface VirtualDiskService {
    VirtualDiskSummaryResponseDto create(VirtualDiskRequestDto request);
}

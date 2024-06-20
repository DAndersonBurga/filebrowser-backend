package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.controller.request.CreateVirtualDiskRequest;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface VirtualDiskService {
    VirtualDiskSummaryResponse create(CreateVirtualDiskRequest request);
    List<FileResponse> findFiles(UUID id);
}

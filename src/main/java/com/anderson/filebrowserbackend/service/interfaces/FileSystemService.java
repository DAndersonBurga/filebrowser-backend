package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;

import java.util.List;


public interface FileSystemService {
    List<VirtualDiskSummaryResponse> getVirtualDisks();
}
